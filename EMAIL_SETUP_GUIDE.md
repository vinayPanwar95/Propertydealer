# Email Configuration Setup Guide

## Overview
The property dealer application now sends emails when users submit contact form enquiries. Emails are sent to the admin and confirmation emails are sent to the user.

## What Happens When User Submits Form:
1. User submits contact form from `/contact.html`
2. Form data is sent to backend (`/contact` endpoint) via POST request
3. Backend validates the data
4. **Email 1**: Admin email sent to `shreeenathjiassociates@gmail.com` with user enquiry details
5. **Email 2**: Confirmation email sent to user's email address
6. Success message displayed to user

## Backend Files Created:
- `ContactController.java` - Handles form submission and validation
- `EmailService.java` - Manages email sending functionality
- `Contact.java` - Model class for contact form data

## Configuration Required:

### Step 1: Update application.properties
File: `src/main/resources/application.properties`

The following email configuration has been added:
```properties
# Email Configuration (Gmail SMTP)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=shreeenathjiassociates@gmail.com
spring.mail.password=YOUR_GMAIL_APP_PASSWORD
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true

# Custom Email Configuration
app.mail.from=shreeenathjiassociates@gmail.com
app.mail.recipient=shreeenathjiassociates@gmail.com
```

⚠️ **IMPORTANT**: Replace `YOUR_GMAIL_APP_PASSWORD` with an actual Gmail App Password (NOT your Gmail password)

### Step 2: Get Gmail App Password
1. Go to myaccount.google.com
2. Click "Security" in the left sidebar
3. Enable "2-Step Verification" if not already enabled
4. Go back to Security → App passwords
5. Select "Mail" and "Windows Computer"
6. Google will generate a 16-character password
7. Copy this password and paste it in `application.properties` as `spring.mail.password`

### Step 3: Update pom.xml
The following dependency has been added to `pom.xml`:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>
```

### Step 4: Build and Run
```bash
# Navigate to project directory
cd property-dealer-full

# Clean, build, and run
mvn clean install
mvn spring-boot:run
```

## Email Templates:

### Email to Admin (shreeenathjiassociates@gmail.com):
Subject: "New Property Enquiry from [User Name]"

Body contains:
- User's full name
- Phone number
- Email address
- Property type they're interested in
- Their enquiry message
- Reply-to address automatically set to user's email

### Confirmation Email to User:
Subject: "Thank you for your enquiry - Shree Nath Ji Associates"

Body contains:
- Personalized greeting
- Property type they enquired about
- Confirmation message
- Their enquiry details
- Company contact information

## Testing Email Submission:

1. Run the application: `mvn spring-boot:run`
2. Navigate to `http://localhost:8080/contact.html`
3. Fill in the form:
   - Name: Test User
   - Phone: 9999649161
   - Email: your@email.com
   - Property Type: Farmland
   - Enquiry: Test enquiry message
4. Click "Send Enquiry"
5. You should receive:
   - Confirmation message in browser
   - Admin email at shreeenathjiassociates@gmail.com
   - Confirmation email at your@email.com

## Error Handling:

If email sending fails:
1. Check application.properties for correct Gmail credentials
2. Verify 2-Step Verification is enabled on Gmail account
3. Check that app password is correctly copied (no spaces)
4. Ensure internet connection is active
5. Check application logs for detailed error messages

## Security Notes:

- Gmail app passwords are safer than regular passwords
- Never commit real credentials to version control
- Consider using environment variables for sensitive data:
  ```properties
  spring.mail.username=${MAIL_USERNAME}
  spring.mail.password=${MAIL_PASSWORD}
  ```

## Files Modified:

1. `application.properties` - Added email configuration
2. `pom.xml` - Added spring-boot-starter-mail dependency
3. `contact.html` - Updated form to send data to backend via AJAX

## Files Created:

1. `ContactController.java` - REST endpoint for form submission
2. `EmailService.java` - Email sending service
3. `Contact.java` - Contact form data model

## Troubleshooting:

| Issue | Solution |
|-------|----------|
| "Authentication failed" | Check Gmail app password is correct, enable 2-Step Verification |
| "Connection timeout" | Check internet connection, verify SMTP host is correct |
| Emails not received | Check spam folder, verify email addresses are correct |
| Form submission fails | Check browser console for errors, verify backend is running |

## Next Steps:

1. Configure Gmail App Password
2. Update `application.properties` with app password
3. Run `mvn clean install`
4. Start the application
5. Test the contact form
6. Verify emails are being received

