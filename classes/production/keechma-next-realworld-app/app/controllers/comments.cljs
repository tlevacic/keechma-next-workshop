(ns app.controllers.comments
  (:require [keechma.next.controller :as ctrl]
            [keechma.next.controllers.pipelines :as pipelines]
            [keechma.next.controllers.entitydb :as edb]
            [keechma.next.controllers.dataloader :as dl]
            [keechma.next.toolbox.pipeline :as pp :refer [pswap! preset!] :refer-macros [pipeline!]]
            [app.api :as api]
            [app.settings :as settings]))

(derive :comments ::pipelines/controller)

(def dataloader-options
  {:keechma.dataloader/max-stale true
   :keechma.dataloader/stale-while-revalidate true})

(def pipelines
  {:keechma.on/start (-> (pipeline! [value ctrl]
                           (dl/req ctrl :dataloader api/comments-get {:article-slug value} dataloader-options)
                           (edb/insert-collection! ctrl :entitydb :comment :comment/list value))
                         (pp/set-queue :load-comments))
   :keechma.on/stop (pipeline! [_ ctrl]
                      (edb/remove-collection! ctrl :entitydb :comment/list))})

(defmethod ctrl/prep :comments [ctrl]
  (pipelines/register ctrl pipelines))

(defmethod ctrl/derive-state :comments [_ state {:keys [entitydb]}]
  (edb/get-collection entitydb :comment/list [(edb/include :author)]))