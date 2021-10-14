(ns pingcrm.shared.layout
  (:require [pingcrm.shared.bottom-header :refer [bottom-header]]
            [pingcrm.shared.flash-messages :refer [flash-messages]]
            [pingcrm.shared.menu :refer [main-menu]]
            [pingcrm.shared.top-header :refer [top-header]]
            ["@inertiajs/inertia-react" :refer [InertiaLink]]
            [pingcrm.shared.logo :refer [logo]]))

(defn layout
  [children]
  [:div
   [:div {:class "md:flex md:flex-col"}
    [:div {:class "md:h-screen md:flex md:flex-row"}
     [:div {:class "md:flex md:flex-col md:flex-shrink-0"}
      [:div {:class "bg-indigo-900 md:flex-shrink-0 md:h-36 px-2 py-4 flex items-center justify-between md:justify-center"}
       [:> InertiaLink {:class "", :href "/"}
        [logo {:class "fill-white transform rotate-90", :height "120", :width "28"}]]]
      [:f> bottom-header]]
     [:div {:class "flex flex-row flex-grow overflow-hidden mode-lr"}
      [:f> main-menu {:class "flex-shrink-0 hidden h-36 pl-32 pt-3 overflow-x-auto bg-indigo-800 md:block"}]
      ;; To reset scroll region (https://inertiajs.com/pages#scroll-regions) add
      ;; `scroll-region="true"` to div below
      [:div {:class "h-full py-4 px-8 overflow-hidden overflow-y-auto md:p-12"}
       [:f> flash-messages]
       children]]]]])
