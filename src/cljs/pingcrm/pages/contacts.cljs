(ns pingcrm.pages.contacts
  (:require ["@inertiajs/inertia" :refer [Inertia]]
            ["@inertiajs/inertia-react" :refer [InertiaLink useForm Head]]
            [applied-science.js-interop :as j]
            [pingcrm.shared.buttons :refer [loading-button delete-button]]
            [pingcrm.shared.form-input :refer [text-input select-input]]
            [pingcrm.shared.icon :refer [icon]]
            [pingcrm.shared.pagination :refer [pagination]]
            [pingcrm.shared.search-filter :refer [search-filter]]
            [pingcrm.shared.trashed-message :refer [trashed-message]]))

(defn index [{:keys [contacts]}]
  (let [{:keys [data links]} (j/lookup contacts)]
    [:div
     [:> Head {:title "Contacts"}]
     [:h1 {:class "mr-8 text-3xl font-bold"} "Contacts"]
     [:div {:class "flex items-center justify-between mr-6 mode-lr"}
      ; [:f> search-filter]
      [:> InertiaLink
       {:class "btn-indigo focus:outline-none",
        :href (js/route "contacts.create")} [:span "Create "]
       [:span {:class "hidden md:inline"} "Contact"]]]
     [:div {:class "overflow-y-auto bg-white rounded shadow"}
      [:table {:class "h-full whitespace-nowrap"}
       [:thead
        [:tr {:class "font-bold text-left"}
         [:th {:class "py-6 pl-5 pr-4"} "Name"]
         [:th {:class "py-6 pl-5 pr-4"} "Organization"]
         [:th {:class "py-6 pl-5 pr-4"} "City"]
         [:th {:class "py-6 pl-5 pr-4", :col-span "2"} "Phone"]]]
       [:tbody
        (for [contact data
              :let [{:keys [id name city phone organization deleted_at]} (j/lookup contact)]]
          [:tr {:class "hover:bg-gray-100 focus-within:bg-gray-100"
                :key id}
           [:td {:class "border-t"}
            [:> InertiaLink {:href (js/route "contacts.edit" id)
                             :class "flex items-center py-6 px-4 focus:text-indigo-700 focus:outline-none"}
             name
             (when deleted_at
               [icon {:name :trash
                      :class "flex-shrink-0 h-3 w-3 mt-2 text-gray-400 fill-current"}])]]
           [:td {:class "border-t"}
            [:> InertiaLink {:href (js/route "contacts.edit" id)
                             :tab-index "-1"
                             :class "flex items-center py-6 px-4 focus:text-indigo-700 focus:outline-none"}
             (when organization (j/get organization :name))]]
           [:td {:class "border-t"}
            [:> InertiaLink {:href (js/route "contacts.edit" id)
                             :tab-index "-1"
                             :class "flex items-center py-6 px-4 focus:text-indigo-700 focus:outline-none"}
             city]]
           [:td {:class "border-t"}
            [:> InertiaLink {:href (js/route "contacts.edit" id)
                             :tab-index "-1"
                             :class "flex items-center py-6 px-4 focus:text-indigo-700 focus:outline-none"}
             phone]]
           [:td {:class "w-px border-t"}
            [:> InertiaLink {:href (js/route "contacts.edit" id)
                             :tab-index "-1"
                             :class "flex items-center py-4 focus:outline-none"}
             [icon {:name :cheveron-right
                    :class "block w-6 h-6 text-gray-400 fill-current"}]]]])
        (when (zero? (count data))
          [:tr
           [:td {:class "py-6 px-4 border-t"
                 :col-span "4"}
            "No contacts found."]])]]]
     [pagination links]]))

(defn create-form [^js organizations]
  (let [{:keys [data setData errors post processing]}
        (j/lookup (useForm #js {:first_name ""
                                :last_name ""
                                :organization_id ""
                                :email ""
                                :phone ""
                                :address ""
                                :city ""
                                :region ""
                                :country ""
                                :postal_code ""}))
        on-submit #(do (.preventDefault %)
                       (post (js/route "contacts.store")))]
    [:div
     [:> Head {:title "Create Contact"}]
     [:h1 {:class "ml-8 text-3xl font-bold"}
      [:> InertiaLink {:href (js/route "contacts")
                       :class "text-indigo-400 hover:text-indigo-600"}
       "Contacts"]
      [:span {:class ""} " / "]
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
        [select-input {:class "h-full pl-8 pb-6 lg:h-1/2"
                       :label "Organization"
                       :name "organization_id"
                       :errors (.-organization_id errors)
                       :value (.-organization_id data)
                       :on-change #(setData "organization_id" (.. % -target -value))}
          [organizations]]
        [text-input {:class "h-full pl-8 pb-6 lg:h-1/2"
                     :label "Email"
                     :name "email"
                     :errors (.-email errors)
                     :value (.-email data)
                     :on-change #(setData "email" (.. % -target -value))}]
        [text-input {:class "h-full pl-8 pb-6 lg:h-1/2"
                     :label "Phone"
                     :name "phone"
                     :errors (.-phone errors)
                     :value (.-phone data)
                     :on-change #(setData "phone" (.. % -target -value))}]
        [text-input {:class "h-full pl-8 pb-6 lg:h-1/2"
                     :label "Address"
                     :name "address"
                     :errors (.-address errors)
                     :value (.-address data)
                     :on-change #(setData "address" (.. % -target -value))}]
        [text-input {:class "h-full pl-8 pb-6 lg:h-1/2"
                     :label "City"
                     :name "city"
                     :errors (.-city errors)
                     :value (.-city data)
                     :on-change #(setData "city" (.. % -target -value))}]
        [text-input {:class "h-full pl-8 pb-6 lg:h-1/2"
                     :label "Province/State"
                     :name "region"
                     :errors (.-region errors)
                     :value (.-region data)
                     :on-change #(setData "region" (.. % -target -value))}]
        [select-input {:class "h-full pl-8 pb-6 lg:h-1/2"
                       :label "Country"
                       :name "country"
                       :errors (.-country errors)
                       :value (.-country data)
                       :on-change #(setData "country" (.. % -target -value))}
          [(clj->js [{:id "1" :name "Canada"}
                     {:id "2" :name "United States"}])]]
        [text-input {:class "h-full pl-8 pb-6 lg:h-1/2"
                     :label "Postal code"
                     :name "postal_code"
                     :errors (.-postal_code errors)
                     :value (.-postal_code data)
                     :on-change #(setData "postal_code" (.. % -target -value))}]]
       [:div {:class "py-8 px-4 bg-gray-50 border-t border-gray-100 flex justify-end items-center mode-lr"}
        [loading-button {:loading processing
                         :type "submit"
                         :class "btn-indigo"}
         "Create Contact"]]]]]))

(defn edit-form [^js contact ^js organizations]
  (let [{:keys [data setData errors put processing]}
        (j/lookup (useForm #js {:first_name (or (.-first_name contact) "")
                                :last_name (or (.-last_name contact) "")
                                :organization_id (or (.-organization_id contact) "")
                                :email (or (.-email contact) "")
                                :phone (or (.-phone contact) "")
                                :address (or (.-address contact) "")
                                :city (or (.-city contact) "")
                                :region (or (.-region contact) "")
                                :country (or (.-country contact) "")
                                :postal_code (or (.-postal_code contact) "")}))
        on-submit #(do (.preventDefault %)
                       (put (js/route "contacts.update" (.-id contact))))
        destroy #(when (js/confirm "Are you sure you want to delete this contact?")
                   (.delete Inertia (js/route "contacts.destroy" (.-id contact))))
        restore #(when (js/confirm "Are you sure you want to restore this contact?")
                   (.put Inertia (js/route "contacts.restore" (.-id contact))))]
    [:div
     [:> Head {:title (str (j/get contact :first_name) " " (j/get contact :last_name))}]
     [:h1 {:class "ml-8 text-3xl font-bold"}
      [:> InertiaLink {:href (js/route "contacts")
                       :class "text-indigo-600 hover:text-indigo-700"}
       "Contacts"]
      [:span {:class "my-2 font-medium text-indigo-600"}
       "/"]
      (.-first_name data) " " (.-last_name data)]
     (when-not (empty? (j/get contact :deleted_at))
       [trashed-message {:on-restore restore}
        "This contact has been deleted."])
     [:div {:class "max-h-3xl overflow-hidden bg-white rounded shadow"}
      [:form {:on-submit on-submit}
       [:div {:class "flex flex-row flex-wrap p-8 -ml-8 -mb-6"}
        [text-input {:class "w-full h-full pl-8 pb-6 lg:h-1/2"
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
        [select-input {:class "h-full pl-8 pb-6 lg:h-1/2"
                       :label "Organization"
                       :name "organization_id"
                       :errors (.-organization_id errors)
                       :value (.-organization_id data)
                       :on-change #(setData "organization_id" (.. % -target -value))}
         [organizations]]
        [text-input {:class "h-full pl-8 pb-6 lg:h-1/2"
                     :label "Email"
                     :name "email"
                     :errors (.-email errors)
                     :value (.-email data)
                     :on-change #(setData "email" (.. % -target -value))}]
        [text-input {:class "h-full pl-8 pb-6 lg:h-1/2"
                     :label "Phone"
                     :name "phone"
                     :errors (.-phone errors)
                     :value (.-phone data)
                     :on-change #(setData "phone" (.. % -target -value))}]
        [text-input {:class "h-full pl-8 pb-6 lg:h-1/2"
                     :label "Address"
                     :name "address"
                     :errors (.-address errors)
                     :value (.-address data)
                     :on-change #(setData "address" (.. % -target -value))}]
        [text-input {:class "h-full pl-8 pb-6 lg:h-1/2"
                     :label "City"
                     :name "city"
                     :errors (.-city errors)
                     :value (.-city data)
                     :on-change #(setData "city" (.. % -target -value))}]
        [text-input {:class "h-full pl-8 pb-6 lg:h-1/2"
                     :label "Province/State"
                     :name "region"
                     :errors (.-region errors)
                     :value (.-region data)
                     :on-change #(setData "region" (.. % -target -value))}]
        [select-input {:class "h-full pl-8 pb-6 lg:h-1/2"
                       :label "Country"
                       :name "country"
                       :type "text"
                       :errors (.-country errors)
                       :value (.-country data)
                       :on-change #(setData "country" (.. % -target -value))}
          [(clj->js [ {:id "" :name ""}
                      {:id "1" :name "Canada"}
                      {:id "2" :name "United States"}])]]
        [text-input {:class "h-full pl-8 pb-6 lg:h-1/2"
                     :label "Postal Code"
                     :name "postal_code"
                     :errors (.-postal_code errors)
                     :value (.-postal_code data)
                     :on-change #(setData "postal_code" (.. % -target -value))}]]
       [:div {:class "flex items-center py-8 px-4 bg-gray-100 border-r border-gray-200"}
        (when (empty? (j/get contact :deleted_at))
          [delete-button {:on-delete destroy}
           "Delete Contact"])
        [loading-button {:loading processing
                         :type "submit"
                         :class "mt-auto btn-indigo"}
         "Update Contact"]]]]]))

(defn edit [{:keys [contact organizations]}]
  [:f> edit-form contact organizations])

(defn create [{:keys [organizations]}]
  [:f> create-form organizations])
