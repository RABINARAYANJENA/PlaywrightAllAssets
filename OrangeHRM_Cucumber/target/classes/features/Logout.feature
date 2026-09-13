Feature: Logout

Scenario: Logout Successfully

Given User opens Chrome browser3
When User opens OrangeHRM website3
And User enters username3
And User enters password3
And User clicks Login button3
And User clicks Profile3
And User clicks Logout3
Then User should return to Login page3