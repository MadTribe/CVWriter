<#-- cv_template.md.ftl -->
# ${cv.fullName()} - Curriculum Vitae - Technology Director

**${cv.contact().email()}** | ${cv.contact().phoneNumber()} |  
${cv.contact().linkedIn()} | London, UK

---

## **Professional Summary**

${cv.professionalSummary()}

---

## **Key Achievements**

<#list cv.keyAchievements() as achievement>
* ${achievement}
  </#list>

---

## **Professional Experience**

<#list cv.employers() as employer>
### **${employer.name()}** (${employer.location()})

<#list employer.positions() as position>
**${position.title()}** (${position.period().from()}–${position.period().to()})
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
    * ${achievement}
      </#list>
      </#list>
      </#if>
      </#list>
      </#list>

---

## **Education**

<#list cv.educations() as education>
* **${education.title()}**  
  ${education.institution()} (${education.period().from()}–${education.period().to()})
  </#list>

---

## ** Skills**

<#list cv.technicalSkills()?keys as category>
### **${category}**
<#list cv.technicalSkills()[category] as skill>
* ${skill}
  </#list>

</#list>

---

## **Languages**

<#list cv.spokenLanguages() as language>
* ${language}
  </#list>