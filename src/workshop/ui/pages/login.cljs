(ns workshop.ui.pages.login
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
            [workshop.ui.components.modal :refer [Modal]]))

(defnc LoginRenderer
       [props]
       (let [role (use-sub props :role)]
         (when (= role :user)
           (dispatch props :router :redirect! {:page "inbox"}))
         (d/div
           {:class "col-12 flex justify-center"
            :style {:height      "100vh"
                    :align-items "center"}}
           (d/div
             {:class "col-6"}
             (d/h1
               {:class "text-xs-center pb4"}
               "Sign in")
             (d/form
               {:on-submit (fn [e]
                             (.preventDefault e)
                             (dispatch props :login-form :keechma.form/submit))}
               (wrapped-input {:keechma.form/controller :login-form
                               :input/type              :text
                               :input/attr              :email
                               :placeholder             "Email"})
               (wrapped-input {:keechma.form/controller :login-form
                               :input/type              :text
                               :input/attr              [:password]
                               :type                    "password"
                               :placeholder             "Password"})
               (d/button
                 {:class "btn btn-lg"
                  :style {:margin  "auto"
                          :display "block"}} "Sign In"))))))

(def Login (with-keechma LoginRenderer))