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
  (filterv #(= true (:isImportant %)) emails))

(defn filter-sent-emails [emails]
  (filterv #(= current-user (:from %)) emails))

(defn filter-data [emails page]
  (case page
    "inbox" emails
    "important" (filter-important-emails emails)
    "sent" (filter-sent-emails emails)
    emails))


(defnc MessagesRenderer [props]
       (let [{:keys [page subpage box]} (use-sub props :router)
             data (filter-data emails page)]
         (d/div (map (fn [ele]
                       (d/div
                         (d/p "Important " (str (:isImportant ele)))
                         (d/p "Sent " (:from ele)))) data))))

(def Messages (with-keechma MessagesRenderer))
