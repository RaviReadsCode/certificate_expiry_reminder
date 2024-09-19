# Certificate Expiry Reminder

---

### Overview

This repository provides a Groovy script designed to monitor SSL certificate expiration dates. The script sends an email alert **24 days** before the certificate's expiration, ensuring timely renewal.

---

### Components

1. **`certificates.csv`**
2. **`certificate_expiry.groovy`**

---

### File Descriptions

- **`certificates.csv`**  
  This file contains the URLs of certificates and their respective expiration dates. The structure is as follows:
  - **Column 1:** URL of the certificate (e.g., `https://example.com`)
  - **Column 2:** Expiry date of the certificate (format: `YYYY-MM-DD`)

- **`certificate_expiry.groovy`**  
  A Jenkins pipeline script that reads the `certificates.csv` file, checks for upcoming expirations, and sends email notifications to alert recipients. The alert is triggered when the expiration date is within 24 days.

---

### How It Works

1. **Certificate Monitoring**  
   The `certificate_expiry.groovy` script reads the URLs and expiration dates from `certificates.csv` and calculates the number of days until expiration.

2. **Alert System**  
   If a certificate is set to expire within the next 24 days, the script triggers an email alert to the configured list of recipients.

---

### Setup Instructions

1. Add your certificate URLs and expiry dates to `certificates.csv`.
2. Configure the email recipients in the `certificate_expiry.groovy` script.
3. Set up the Jenkins job to run periodically using a cron schedule (e.g., daily at midnight).
4. Receive timely email alerts to ensure certificate renewals are handled before expiration.

---

### Example CSV Format

```
https://example.com,2025-06-25
https://anotherdomain.com,2025-05-30
```

---

### Scheduling in Jenkins

Use the following cron expression to schedule the job daily at midnight:

```
H 0 * * *
```

---

Ensure your certificates never expire without notice with this simple and effective Jenkins-based solution.

---
