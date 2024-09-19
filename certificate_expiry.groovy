import java.text.SimpleDateFormat
import java.util.Date
import groovy.json.JsonOutput

// Configuration
def emailRecipients = ['kr8856264@gmail.com']
def daysBeforeExpiryAlert = 10
def csvFilePath = 'https://github.com/RaviReadsCode/certificate_expiry_reminder/blob/test/certificates.csv'
def currentDate = new Date()

// Helper function to send email
def sendEmail(recipients, subject, body) {
    def mailCommand = "echo '${body}' | mail -s '${subject}' ${recipients.join(' ')}"
    println "Sending email: ${mailCommand}"
    mailCommand.execute()
}

// Function to check expiry and send alerts
def checkExpiryAndAlert() {
    def csvFile = new File(csvFilePath)
    if (!csvFile.exists()) {
        println "CSV file not found!"
        return
    }

    def lines = csvFile.readLines()
    def header = lines[0].split(',')
    def urlIndex = header.indexOf('URL')
    def expiryIndex = header.indexOf('ExpiryDate')

    lines[1..-1].each { line ->
        def cols = line.split(',')
        def url = cols[urlIndex]
        def expiryDateStr = cols[expiryIndex]

        def sdf = new SimpleDateFormat("yyyy-MM-dd")
        def expiryDate = sdf.parse(expiryDateStr)
        def diffInMilliseconds = expiryDate.time - currentDate.time
        def daysUntilExpiry = diffInMilliseconds / (1000 * 60 * 60 * 24)

        if (daysUntilExpiry <= daysBeforeExpiryAlert) {
            def subject = "SSL Certificate Expiry Alert for ${url}"
            def body = "The SSL certificate for ${url} is expiring in ${daysUntilExpiry} days on ${expiryDateStr}. Please renew it soon."
            sendEmail(emailRecipients, subject, body)
        }
    }
}

checkExpiryAndAlert()
