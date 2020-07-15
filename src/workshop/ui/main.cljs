(ns workshop.ui.main
  (:require [keechma.next.helix.core :refer [with-keechma dispatch use-sub]]
            [keechma.next.helix.lib :refer [defnc]]
            [helix.core :as hx :refer [$ <>]]
            ["react" :as react]
            ["react-dom" :as rdom]
            [keechma.next.controllers.router :as router]
            [helix.dom :as d]
            [app.ui.components.inputs :refer [wrapped-input]]
            [workshop.ui.components.sidebar :refer [Sidebar]]
            [workshop.ui.components.compose :refer [Compose]]
            [workshop.ui.components.messages :refer [Messages]]
            [workshop.ui.pages.login :refer [Login]]
            [workshop.ui.components.modal :refer [Modal]]))

(defnc UserPages [{:keys [form?] :as props}]
       (let [role (use-sub props :role)]
         (if (= role :guest)
           (dispatch props :router :redirect! {:page "login"})
           (<>
             ($ Sidebar)
             ($ Messages)
             ($ Compose)
             (when form?
               ($ Modal {& props}))))))

(defnc MainRenderer [props]
       (let [route (use-sub props :router)
             form? (:form route)
             page (:page route)]
         (d/div {:class "flex flex-row col-12"}
                (case page
                  "login" ($ Login {& props})
                  ($ UserPages {:form? form? & props})))))

(def Main (with-keechma MainRenderer))