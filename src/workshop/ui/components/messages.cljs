(ns workshop.ui.components.messages
  (:require [keechma.next.helix.core :refer [with-keechma use-sub dispatch]]
            [keechma.next.helix.lib :refer [defnc]]
            [helix.core :as hx :refer [$ <> suspense]]
            [helix.dom :as d]
            ["react" :as react]
            ["react-dom" :as rdom]
            [app.settings :as settings]
            [keechma.next.controllers.router :as router]
            [clojure.string :as str]
            [workshop.data :refer [emails]]))

(def current-user "Camden_Schulist@hotmail.com")

(defn filter-important-emails [emails]
  (filterv #(:isImportant %) emails))

(defn filter-sent-emails [emails]
  (filterv #(= current-user (:from %)) emails))

(defn filter-data [emails page]
  (case page
    "inbox" emails
    "important" (filter-important-emails emails)
    "sent" (filter-sent-emails emails)
    emails))

(defnc Message [{:keys [email] :as props}]
       (let [important (:isImportant email)]
         (d/div {:class "flex flex-row mx2 px2"}
                (d/div {:class "px1 border flex justify-center"
                        :style {:align-items "center"
                                :border-color "gray"}}
                       (d/img {:width  "50px"
                               :height "50px"
                               :src    (:avatar email)}))
                (d/div {:class "flex flex-column border col-12 pl2 pt2"
                        :style {:border-color "gray"}}
                       (if (true? important)
                         (d/h4 (d/span {:class "px2 mr2 rounded"
                                        :style {:color "white"
                                                :width "110px"
                                                :font-size "1.3rem"
                                                :background-color "rgb(71,183,215)"}} "IMPORTANT")(:subject email))
                         (d/h4 (:subject email)))
                       (d/p (:from email))))))


(defnc MessagesRenderer [props]
       (let [{:keys [page subpage box]} (use-sub props :router)
             data (filter-data emails page)]
         (d/div
           (map
             (fn [ele]
               ($ Message {:email ele & props})) data))))

(def Messages (with-keechma MessagesRenderer))
