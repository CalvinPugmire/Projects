package org.example.ses;

// these are the imports for SDK v1
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.simpleemail.*;
import com.amazonaws.services.simpleemail.model.*;
import com.amazonaws.regions.Regions;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class EmailSender {
    public EmailResult handleRequest(EmailRequest request, Context context) {

        LambdaLogger logger = context.getLogger();
        logger.log("Entering send_email");

        EmailResult result;

        try {
            AmazonSimpleEmailService client =
                    AmazonSimpleEmailServiceClientBuilder.standard()

                            // Replace US_WEST_2 with the AWS Region you're using for
                            // Amazon SES.
                            .withRegion(Regions.US_EAST_2).build();

            // TODO:
            // Use the AmazonSimpleEmailService object to send an email message
            // using the values in the EmailRequest parameter object
            SendEmailRequest sendrequest = new SendEmailRequest()
                    .withDestination(
                            new Destination().withToAddresses(request.to))
                    .withMessage(new Message()
                            .withBody(new Body()
                                    .withHtml(new Content()
                                            .withCharset("UTF-8").withData(request.htmlBody))
                                    .withText(new Content()
                                            .withCharset("UTF-8").withData(request.textBody)))
                            .withSubject(new Content()
                                    .withCharset("UTF-8").withData(request.subject)))
                    .withSource(request.from)
                    // Comment or remove the next line if you are not using a
                    // configuration set
                    /*.withConfigurationSetName(CONFIGSET)*/;
            client.sendEmail(sendrequest);

            logger.log("Email sent!");
            result = new EmailResult("Email sent!", Timestamp.valueOf(LocalDateTime.now()).toString());
        } catch (Exception ex) {
            logger.log("The email was not sent. Error message: " + ex.getMessage());
            result = new EmailResult("The email was not sent. Error message: " + ex.getMessage(), Timestamp.valueOf(LocalDateTime.now()).toString());
            throw new RuntimeException(ex);
        }
        finally {
            logger.log("Leaving send_email");
        }

        // TODO:
        // Return EmailResult
        return result;
    }
}