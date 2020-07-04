(ns workshop.ui.main
  (:require [keechma.next.helix.core :refer [with-keechma use-sub]]
            [keechma.next.helix.lib :refer [defnc]]
            [helix.core :as hx :refer [$ <>]]
            ["react" :as react]
            ["react-dom" :as rdom]
            [keechma.next.controllers.router :as router]
            [helix.dom :as d]
            [workshop.ui.components.sidebar :refer [Sidebar]]
            [workshop.ui.components.messages :refer [Messages]]))

(defnc MainRenderer
  [props]
       (<>
         ($ Sidebar)
         ($ Messages)))

(def Main (with-keechma MainRenderer))