(ns workshop.app
  (:require ["react-dom" :as rdom]
            [keechma.next.controllers.router]
            [keechma.next.controllers.subscription]
            [keechma.next.controllers.entitydb]
            [keechma.next.controllers.dataloader]))

(def app
  {:keechma.subscriptions/batcher rdom/unstable_batchedUpdates
   :keechma/controllers           {
                                   :router {:keechma.controller/params true
                                            :keechma.controller/type   :keechma/router
                                            :keechma/routes            [["" {:page "home"}]
                                                                        ":page"
                                                                        ":page/:subpage"
                                                                        ":page/:subpage/:box" ;;This will be when box for message is opend
                                                                        ]}}
   :keechma/apps                  {}})