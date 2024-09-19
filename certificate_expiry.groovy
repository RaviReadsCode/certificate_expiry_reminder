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
                    def csvFile = readFile('certificates.csv')
                    def currentDate = new Date()

                    // Process each line of the CSV file
                    csvFile.splitEachLine(',') { fields ->
                        if (fields.size() >= 2) { // Ensure there are at least 2 columns
                            def url = fields[0].trim()
                            def expiryDateStr = fields[1].trim()
                            try {
                                def expiryDate = Date.parse('yyyy-MM-dd', expiryDateStr)
                                def alertDate = expiryDate - ALERT_DAYS_BEFORE

                                if (currentDate >= alertDate && currentDate < expiryDate) {
                                    sendEmailAlert(url, expiryDate)
                                }
                            } catch (Exception e) {
                                echo "Error processing date for URL ${url}: ${e.message}"
                            }
                        } else {
                            echo "Invalid format in CSV file: ${fields}"
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
