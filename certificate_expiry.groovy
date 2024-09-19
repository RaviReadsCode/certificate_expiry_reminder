pipeline {
    agent any
    environment {
        EMAIL_LIST = 'kr8856264@gmail.com'
        ALERT_DAYS_BEFORE = 10 // Days before expiry to trigger an alert
    }
    stages {
        stage('Check SSL Expiry') {
            steps {
                script {
                    def linksFile = readFile 'certificates.csv'
                    def currentDate = new Date()
                    def alertDate

                    linksFile.splitEachLine(',') { fields ->
                        def url = fields[0].trim()
                        def expiryDate = Date.parse('yyyy-MM-dd', fields[1].trim())
                        alertDate = expiryDate - ALERT_DAYS_BEFORE

                        if (currentDate >= alertDate && currentDate < expiryDate) {
                            sendEmailAlert(url, expiryDate)
                        }
                    }
                }
            }
        }
    }
}

def sendEmailAlert(String url, Date expiryDate) {
    emailext subject: "SSL Certificate Expiry Alert for ${url}",
             body: "The SSL certificate for ${url} is expiring on ${expiryDate}. Please renew it before the expiry date.",
             to: env.EMAIL_LIST
}


