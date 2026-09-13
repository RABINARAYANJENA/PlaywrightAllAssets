Feature: Login with Multiple Users

Scenario Outline: Verify Login

Given User opens Chrome browser2
When User opens OrangeHRM website2
And User enters username2 "<username>"
And User enters password2 "<password>"
And User clicks Login button2
Then Login result should be verified2

Examples:
| username | password |
| Admin    | admin123 |
| Admin    | wrong123 |
| Wrong    | admin123 |