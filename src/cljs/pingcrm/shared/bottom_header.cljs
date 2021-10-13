(ns pingcrm.shared.bottom-header
  (:require ["@inertiajs/inertia-react" :refer [InertiaLink usePage]]
            [applied-science.js-interop :as j]
            [reagent.core :as r]
            [pingcrm.shared.icon :refer [icon]]))

(defn bottom-header
  []
  (let [{:keys [^js auth]} (j/lookup (.-props (usePage)))
        user (.. auth -user)
        opened? (r/atom false)]
    (fn []
      [:div
       {:class
        "bg-white border-l h-full p-4 md:px-0 md:py-6 text-sm md:text-md flex justify-between items-center mode-lr"}
       [:div {:class "ml-1 mb-4"} (.. user -account -name)]
       [:div {:class "relative"}
        [:div
         {:class "flex items-center cursor-pointer select-none group",
          :on-click #(swap! opened? not)}
         [:div
          {:class
           "mb-1 text-gray-800 whitespace-nowrap group-hover:text-indigo-600 focus:text-indigo-600"}
          [:span (.-first_name user)]
          [:span {:class "hidden mt-1 md:inline"} (.-last_name user)]]
         [icon
          {:class
           "w-5 h-5 text-gray-800 fill-current group-hover:text-indigo-600 focus:text-indigo-600",
           :name :cheveron-down}]]
        [:div {:class (when-not @opened? "hidden")}
         [:div
          {:class
           "absolute left-0 bottom-0 left-auto z-20 px-2 ml-8 text-sm whitespace-nowrap bg-white rounded shadow-xl"}
          [:> InertiaLink
           {:href (js/route "users.edit" (.. user -id)),
            :class "block py-6 px-2 hover:bg-indigo-600 hover:text-white",
            :on-click #(swap! opened? not)} "My Profile"]
          [:> InertiaLink
           {:href (js/route "users"),
            :class "block py-6 px-2 hover:bg-indigo-600 hover:text-white",
            :on-click #(swap! opened? not)} "Manage Users"]
          [:> InertiaLink
           {;:as "button",
            :href (js/route "logout"),
            :class
            "block h-full py-6 px-2 text-left focus:outline-none hover:bg-indigo-600 hover:text-white mode-lr",
            :method "delete"} "Logout"]]
         [:div
          {:class "fixed inset-0 z-10 bg-black opacity-25",
           :on-click #(swap! opened? not)}]]]])))
