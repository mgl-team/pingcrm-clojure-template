(ns pingcrm.pages.users
  (:require ["@inertiajs/inertia" :refer [Inertia]]
            ["@inertiajs/inertia-react" :refer [InertiaLink useForm Head]]
            [applied-science.js-interop :as j]
            [pingcrm.shared.buttons :refer [loading-button delete-button]]
            [pingcrm.shared.form-input :refer [text-input select-input]]
            [pingcrm.shared.icon :refer [icon]]
            [pingcrm.shared.search-filter :refer [search-filter]]
            [pingcrm.shared.trashed-message :refer [trashed-message]]))

(defn index [{:keys [users]}]
  [:div
   [:> Head {:title "Users"}]
   [:h1 {:class "ml-8 text-3xl font-bold"} "Users"]
   [:div {:class "flex items-center justify-between ml-6"}
    [:f> search-filter]
    [:> InertiaLink {:class "btn-indigo focus:outline-none"
                     :href (js/route "users.create")}
     [:span "Create"]
     [:span {:class "hidden md:inline"} " User"]]]
   [:div {:class "overflow-y-auto bg-white rounded shadow"}
    [:table {:class "h-full whitespace-nowrap"}
     [:thead
      [:tr {:class "font-bold text-left"}
       [:th {:class "py-6 pr-5 pl-4"} "Name"]
       [:th {:class "py-6 pr-5 pl-4"} "Email"]
       [:th {:class "py-6 pr-5 pl-4" :col-span "2"} "Role"]]]
     [:tbody
      (for [user users
            :let [{:keys [id name email owner deleted_at]} (j/lookup user)]]
        [:tr {:class "hover:bg-gray-100 focus-within:bg-gray-100"
              :key id}
         [:td {:class "border-r"}
          [:> InertiaLink {:href (js/route "users.edit" id)
                           :class "flex items-center py-6 px-4 focus:text-indigo-700 focus:outline-none"}
           name
           (when deleted_at
             [icon {:name :trash
                    :class "flex-shrink-0 w-3 h-3 mt-2 text-gray-400 fill-current"}])]]
         [:td {:class "border-r"}
          [:> InertiaLink {:href (js/route "users.edit" id)
                           :tab-index "-1"
                           :class "flex items-center py-6 px-4 focus:text-indigo-700 focus:outline-none"}
           email]]
         [:td {:class "border-r"}
          [:> InertiaLink {:href (js/route "users.edit" id)
                           :tab-index "-1"
                           :class "flex items-center py-6 px-4 focus:text-indigo-700 focus:outline-none"}
           (if owner "Owner" "User")]]
         [:td {:class "h-px border-r"}
          [:> InertiaLink {:href (js/route "users.edit" id)
                           :tab-index "-1"
                           :class "flex items-center py-4 focus:outline-none"}
           [icon {:name :cheveron-right
                  :class "block w-6 h-6 text-gray-400 fill-current"}]]]])
      (when (zero? (count users))
        [:tr
         [:td {:class "py-6 px-4 border-r"
               :col-span "4"}
          "No Users found."]])]]]])

(defn create-form []
  (let [{:keys [data setData errors post processing]}
        (j/lookup (useForm #js {:first_name ""
                                :last_name ""
                                :email ""
                                :password ""
                                :owner "0"}))
        on-submit #(do (.preventDefault %)
                       (post (js/route "users.store")))]
    [:div
     [:> Head {:title "Create User"}]
     [:h1 {:class "ml-8 text-3xl font-bold"}
      [:> InertiaLink {:href (js/route "users")
                       :class "text-indigo-400 hover:text-indigo-600"}
       "Users" [:span {:class "font-medium text-indigo-400"} " / "]]
      "Create"]
     [:div {:class "max-h-3xl overflow-hidden bg-white rounded shadow"}
      [:form {:on-submit on-submit}
       [:div {:class "flex flex-wrap p-8 -ml-8 -mb-6"}
        [text-input {:class "h-full pl-8 pb-6 lg:h-1/2"
                     :label "First name"
                     :name "first_name"
                     :errors (.-first_name errors)
                     :value (.-first_name data)
                     :on-change #(setData "first_name" (.. % -target -value))}]
        [text-input {:class "h-full pl-8 pb-6 lg:h-1/2"
                     :label "Last name"
                     :name "last_name"
                     :errors (.-last_name errors)
                     :value (.-last_name data)
                     :on-change #(setData "last_name" (.. % -target -value))}]
        [text-input {:class "h-full pl-8 pb-6 lg:h-1/2"
                     :label "Email"
                     :name "email"
                     :errors (.-email errors)
                     :value (.-email data)
                     :on-change #(setData "email" (.. % -target -value))}]
        [text-input {:class "h-full pl-8 pb-6 lg:h-1/2"
                     :label "Password"
                     :name "password"
                     :type "password"
                     :errors (.-password errors)
                     :value (.-password data)
                     :on-change #(setData "password" (.. % -target -value))}]
        [select-input {:class "h-full pl-8 pb-6 lg:h-1/2"
                         :label "Owner"
                         :name "owner"
                         :errors (.-owner errors)
                         :value (.-owner data)
                         :on-change #(setData "country" (.. % -target -value))}
          [(clj->js [{:id "1" :name "Yes"}
                     {:id "0" :name "No"}])]]]
       [:div {:class "ml-4 py-8 px-4 bg-gray-50 border-r border-gray-100 flex justify-end items-center"}
        [loading-button {:loading processing
                         :type "submit"
                         :class "btn-indigo"}
         "Create User"]]]]]))

(defn edit-form [^js user]
  (let [{:keys [data setData errors post processing]}
        (j/lookup (useForm #js {:first_name (or (.-first_name user) "")
                                :last_name (or (.-last_name user) "")
                                :email (or (.-email user) "")
                                :password (or (.-password user) "")
                                :owner (or (if (.-owner user) "1" "0") "0")}))
        on-submit #(do (.preventDefault %)
                       (post (js/route "users.update" (.-id user))))
        destroy #(when (js/confirm "Are you sure you want to delete this user?")
                   (.delete Inertia (js/route "users.destroy" (.-id user))))
        restore #(when (js/confirm "Are you sure you want to restore this user?")
                   (.put Inertia (js/route "users.restore" (.-id user))))]
    [:<>
     [:> Head {:title (str (j/get user :first_name) " " (j/get user :last_name))}]
     [:div {:class "flex justify-start max-h-lg ml-8"}
      [:h1 {:class "text-3xl font-bold"}
       [:> InertiaLink {:class "text-indigo-400 hover:text-indigo-700"
                        :href (js/route "users")} "Users"]
       [:span {:class "my-2 font-medium text-indigo-400"} "/"]
       (.-first_name data) " " (.-last_name data)]]
     (when-not (empty? (j/get user :deleted_at))
       [trashed-message {:on-restore restore}
        "This user has been deleted."])
     [:div {:class "max-h-3xl overflow-hidden bg-white rounded shadow"}
      [:form {:on-submit on-submit}
       [:div {:class "flex flex-wrap p-8 -ml-8 -mb-6"}
        [text-input {:class "h-full pl-8 pb-6 lg:h-1/2"
                     :label "First name"
                     :name "first_name"
                     :errors (.-first_name errors)
                     :value (.-first_name data)
                     :on-change #(setData "first_name" (.. % -target -value))}]
        [text-input {:class "h-full pl-8 pb-6 lg:h-1/2"
                     :label "Last name"
                     :name "last_name"
                     :errors (.-last_name errors)
                     :value (.-last_name data)
                     :on-change #(setData "last_name" (.. % -target -value))}]
        [text-input {:class "h-full pl-8 pb-6 lg:h-1/2"
                     :label "Email"
                     :name "email"
                     :errors (.-email errors)
                     :value (.-email data)
                     :on-change #(setData "email" (.. % -target -value))}]
        [text-input {:class "h-full pl-8 pb-6 lg:h-1/2"
                     :label "Password"
                     :name "password"
                     :type "password"
                     :errors (.-password errors)
                     :value (.-password data)
                     :on-change #(setData "password" (.. % -target -value))}]
        [select-input {:class "h-full pl-8 pb-6 lg:h-1/2"
                       :label "Owner"
                       :name "owner"
                       :errors (.-owner errors)
                       :value (.-owner data)
                       :on-change #(setData "owner" (.. % -target -value))}
         [(clj->js [{:id "1" :name "Yes"}
                    {:id "0" :name "No"}])]]]
       ;; TODO Add file input
       [:div {:class "flex items-center ml-4 py-8 px-4 bg-gray-100 border-r border-gray-200"}
        (when (empty? (j/get user :deleted_at))
          [delete-button {:on-delete destroy}
           "Delete User"])
        [loading-button {:loading processing
                         :type "submit"
                         :class "mt-auto btn-indigo"}
         "Update User"]]]]]))

(defn create []
  [:f> create-form])

(defn edit [{:keys [user]}]
  [:f> edit-form user])
