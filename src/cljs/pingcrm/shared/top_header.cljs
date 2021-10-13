(ns pingcrm.shared.top-header
  (:require ["@inertiajs/inertia-react" :refer [InertiaLink]]
            [pingcrm.shared.logo :refer [logo]]
            [pingcrm.shared.menu :refer [main-menu]]
            [reagent.core :as r]))

(defn top-header []
  [:div {:class "bg-indigo-900 md:flex-shrink-0 md:h-56 px-2 py-4 flex items-center justify-between md:justify-center"}
   [:> InertiaLink {:class "", :href "/"}
    [logo {:class "fill-white transform rotate-90", :height "120", :width "28"}]]])
