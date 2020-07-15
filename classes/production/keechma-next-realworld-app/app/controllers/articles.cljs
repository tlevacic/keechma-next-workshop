(ns app.controllers.articles
  (:require [keechma.next.controller :as ctrl]
            [keechma.next.controllers.pipelines :as pipelines]
            [keechma.next.controllers.entitydb :as edb]
            [keechma.next.controllers.dataloader :as dl]
            [keechma.next.toolbox.pipeline :as pp :refer [pswap! preset!] :refer-macros [pipeline!]]
            [app.api :as api]
            [app.settings :as settings]))

(derive :articles ::pipelines/controller)

(def dataloader-options
  {:keechma.dataloader/max-stale true
   :keechma.dataloader/stale-while-revalidate true})

(defn add-pagination-param [params {:keys [p]}]
  (if p
    (let [offset (* (dec (js/parseInt p 10)) settings/articles-per-page)]
      (assoc params :offset offset))
    params))

(defn add-tag-param [params {:keys [tag]}]
  (if tag
    (assoc params :tag tag)
    params))

(defn add-author-param [params {:keys [page user detail]}]
  (if (and user (= "profile" page))
    (if (= "favorites" detail)
      (assoc params :favorited user)
      (assoc params :author user))
    params))

(defn get-req-params [router]
  (-> {}
      (add-pagination-param router)
      (add-tag-param router)
      (add-author-param router)))

(defn get-params [deps]
  (let [{:keys [jwt router]} deps
        {:keys [page subpage]} router
        feed-type (if (and jwt (= "home" page) (= "personal" subpage)) :personal :public)]
    {:feed-type feed-type
     :jwt jwt
     :params (get-req-params router)}))

(def load-articles
  (-> (pipeline! [value {:keys [meta-state*] :as ctrl}]
        (pswap! meta-state* assoc :params value)
        (dl/req ctrl :dataloader api/articles-get value dataloader-options)
        (edb/insert-collection! ctrl :entitydb :article :article/list (:data value))
        (pswap! meta-state* assoc :response (:meta value)))
      (pp/set-queue :load-articles)
      pp/use-existing
      pp/restartable))

(def load-articles-if-new-params
  (pipeline! [value {:keys [meta-state* deps-state*] :as ctrl}]
    (get-params @deps-state*)
    (when (not= value (:params @meta-state*))
      load-articles)))

(def pipelines
  {:keechma.on/start load-articles-if-new-params
   :keechma.on/deps-change load-articles-if-new-params
   :keechma.on/stop (pipeline! [_ ctrl]
                      (edb/remove-collection! ctrl :entitydb :article/list))})

(defmethod ctrl/prep :articles [ctrl]
  (pipelines/register ctrl pipelines))

(defmethod ctrl/derive-state :articles [_ state {:keys [entitydb]}]
  (edb/get-collection entitydb :article/list [(edb/include :author)]))