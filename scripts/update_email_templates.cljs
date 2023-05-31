(ns update-email-templates
  (:require ["@aws-sdk/client-ses"
             :refer [SESClient
                     CreateTemplateCommand
                     UpdateTemplateCommand
                     DeleteTemplateCommand]]
            ["fs" :as fs]
            ["path" :as path]
            [promesa.core :as p]))

(def ses-client
  (new SESClient
       #js {:apiVersion "2010-12-01" :region "eu-west-1"}))

(defn ses-send [command]
  (p/let [response (.send ses-client command)
          status   (get-in response ["$metadata" "httpStatusCode"])]
    (when (= status 200)
      (println "DONE"))))

(defn get-template [file-name]
  (.toString
   (fs/readFileSync
    (path/join (path/resolve ".") "emails" file-name))))

(defn update-email-template [{:keys [init subject name env]}]
  (let [html-template  (get-template (str name ".html"))
        plain-template (get-template (str name ".txt"))
        params {:Template
                {:TemplateName (str name "-" env)
                 :SubjectPart  subject
                 :TextPart     plain-template
                 :HtmlPart     html-template}}]
    (new (if init
           CreateTemplateCommand
           UpdateTemplateCommand)
         (clj->js params))))

(ses-send
 (update-email-template
  {:init true :subject "{{title}}" :name "generic" :env "devcljst"}))

;; (ses-send
;;  (new DeleteTemplateCommand
;;       (clj->js {:TemplateName (str "generic-devcljst")})))
