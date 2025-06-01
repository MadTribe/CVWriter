<#-- cv_template.ftlh -->
<!DOCTYPE html>
<html>
<head>
    <title>${cv.fullName()}'s CV</title>
    <style>
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; line-height: 1.6; max-width: 800px; margin: 0 auto; padding: 20px; }
        .header { text-align: center; margin-bottom: 30px; }
        h1 { font-size: 2.2em; margin-bottom: 5px; }
        .subtitle { font-size: 1.4em; color: #444; }
        .contact-info { margin: 15px 0; }
        .section { margin-bottom: 25px; }
        h2 { border-bottom: 2px solid #3498db; padding-bottom: 5px; color: #2c3e50; }
        h3 { margin-bottom: 5px; color: #2980b9; }
        .position-period { font-style: italic; color: #7f8c8d; margin-bottom: 8px; }
        ul { padding-left: 20px; }
        li { margin-bottom: 8px; }
        .skills-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(300px, 1fr)); gap: 20px; }
        .skill-category h4 { margin-bottom: 8px; color: #16a085; }
        .achievement-list li::marker { content: "•"; color: #e74c3c; font-size: 1.2em; }
    </style>
</head>
<body>
    <div class="header">
        <h1>${cv.fullName()}</h1>
        <div class="subtitle">Curriculum Vitae - Technology Director</div>

        <div class="contact-info">
            ${cv.contact().email()} |
            ${cv.contact().phoneNumber()}<br>
            ${cv.contact().linkedIn()} | London, UK
        </div>
    </div>

    <hr>

    <#-- Professional Summary -->
    <div class="section">
        <h2>Professional Summary</h2>
        <p>${cv.professionalSummary()}</p>
    </div>

    <#-- Key Achievements -->
    <div class="section">
        <h2>Key Achievements</h2>
        <ul class="achievement-list">
            <#list cv.keyAchievements() as achievement>
                <li>${achievement}</li>
            </#list>
        </ul>
    </div>

    <#-- Professional Experience -->
    <div class="section">
        <h2>Professional Experience</h2>
        <#list cv.employers() as employer>
            <div class="employer">
                <h3>${employer.name()}</h3>
                <div class="position-period">${employer.period().from()} to ${employer.period().to()}</div>

                <#list employer.positions() as position>
                    <div class="position">
                        <h4>${position.title()}</h4>
                        <div class="position-period">${position.period().from()} to ${position.period().to()}</div>

                        <#if position.description()?has_content>
                            <p>${position.description()}</p>
                        </#if>

                        <ul>
                            <#list position.responsibilities() as responsibility>
                                <li>${responsibility}</li>
                            </#list>
                        </ul>

                        <#if position.projects()?has_content>
                            <h5>Key Projects:</h5>
                            <ul>
                                <#list position.projects() as project>
                                    <li>
                                        <strong>${project.title()}</strong> - ${project.description()}
                                        <#if project.achievements()?has_content>
                                            <ul>
                                                <#list project.achievements() as achievement>
                                                    <li>${achievement}</li>
                                                </#list>
                                            </ul>
                                        </#if>
                                    </li>
                                </#list>
                            </ul>
                        </#if>
                    </div>
                </#list>
            </div>
        </#list>
    </div>

    <#-- Education -->
    <div class="section">
        <h2>Education</h2>
        <#list cv.educations() as education>
            <div class="education">
                <h4>${education.title()}</h4>
                <div class="institution">${education.institution()}</div>
                <div class="period">${education.period().from()} to ${education.period().to()}</div>
            </div>
        </#list>
    </div>


    <#-- Skills -->
    <div class="section">
        <h2>Technical Skills</h2>
        <div class="skills-grid">
            <#list cv.technicalSkills()?keys as category>
                <div class="skill-category">
                    <h4>${category}</h4>
                    <ul>
                        <#list cv.technicalSkills()[category] as skill>
                            <li>${skill}</li>
                        </#list>
                    </ul>
                </div>
            </#list>
        </div>
    </div>

    <#-- Languages -->
    <div class="section">
        <h2>Languages</h2>
        <ul>
            <#list cv.spokenLanguages() as language>
                <li>${language}</li>
            </#list>
        </ul>
    </div>
</body>
</html>