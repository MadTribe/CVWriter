# ${cv.fullName()}

## Contact
Email: ${cv.contact().email()}

Phone: ${cv.contact().phoneNumber()}

## Skills
<#list cv.skills() as skill>
- ${skill.title()}
</#list>
