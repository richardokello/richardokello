package co.ke.tracom.bprgateway.web.accountopening;

/**
 * Account Opening: Function to do micro account opening on a Mobile application and PC Module by agents
 *  - Customer fills a form and hands it over with RWF5000 to an agent
 *  - Enter customer mother branch - Branch management on the BSS
 *  - Fill out customer location details
 *  - Validate customer national ID by checking
 *      Full names | DoB | Gender | Place of ID issue
 *  - Fill out KYC
 *       a. Validation data
 *       b. Phone no
 *       c. Occupation
 *       d. Social Class (1,2,3,4)
 *       e. Education level (None(0), Primary(1), Secondary(2), Technical(3), undergraduate(4), Postgraduate(5) )
 *  - Present confirmation screen
 *  - Confirm using agent PIN before submission
 *  - Submission successful, "pending approval by bank"
 *
 *  On approval
 *   - Send OFS to T24
 *   - Customer signature and photo seen in T24 (This should be from NID platform)
 *   - SMS Client of completion of account opening status: Account number and name
 *   - Inform customer to deposit RWF1000 for account activation
 *
 */