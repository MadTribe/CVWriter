<#-- cv_template.md.ftl -->
# ${cv.fullName()} - Curriculum Vitae - Technology Director

**${cv.contact().email()}** | ${cv.contact().phoneNumber()} |  
${cv.contact().linkedIn()} | Bath, UK

---

## **Professional Summary**

${cv.professionalSummary()}

---

## **Key Achievements**

<#list cv.keyAchievements() as achievement>
* ${achievement.name()}
  </#list>

---

## **Professional Experience**

<#list cv.employers() as employer>
### **${employer.name()}** (${employer.location()})

<#list employer.positions() as position>
**${position.title()}** (${position.period().from()} to ${position.period().to()})
<#if position.description()?has_content>
${position.description()}
</#if>

<#list position.responsibilities() as responsibility>
* ${responsibility}
  </#list>

<#if position.projects()?has_content>
**Key Projects:**  
<#list position.projects() as project>
* **${project.title()}**: ${project.description()}
  <#list project.achievements() as achievement>
    * ${achievement.name()}
      </#list>
      </#list>
      </#if>
      </#list>
      </#list>

---

## **Education**

<#list cv.educations() as education>
* **${education.title()}**  
  ${education.institution()} (${education.period().from()} to ${education.period().to()})
  </#list>

---

## **Skills**

<#list cv.technicalSkills()?keys as category>
### **${category}**
<#list cv.technicalSkills()[category] as skill>
* ${skill.name()}
  </#list>

</#list>

---

## **Languages**

<#list cv.spokenLanguages() as language>
* ${language}
  </#list>