<!DOCTYPE html>
<html>
<head>
    <title>${cv.fullName}'s CV</title>
    <style>
        body { font-family: sans-serif; }
        .section { margin-bottom: 20px; }
        h2 { border-bottom: 1px solid #ccc; }
    </style>
</head>
<body>
    <h1>${cv.fullName}</h1>

    <!-- Contact Section -->
    <div class="section">
        <h2>Contact</h2>
        <p>Email: ${cv.contact.email}</p>
        <p>Phone: ${cv.contact.phoneNumber}</p>
        <p>LinkedIn: ${cv.contact.linkedIn}</p>
    </div>

    <!-- Skills Section -->
    <div class="section">
        <h2>Skills</h2>
        <ul>
            <#list cv.skills as skill>
                <li>${skill.title}</li>
            </#list>
        </ul>
    </div>

    <!-- Employment History -->
    <div class="section">
        <h2>Employment History</h2>
        <#list cv.employers as employer>
            <h3>${employer.name} - ${employer.location}</h3>
            <p>${employer.period.from} to ${employer.period.to}</p>

            <h4>Positions:</h4>
            <#list employer.position as position>
                <p><strong>${position.title}</strong> (${position.period.from} - ${position.period.to})</p>
                <p>${position.description}</p>
            </#list>

            <h4>Projects:</h4>
            <#list employer.projects as project>
                <p><strong>${project.title}</strong> (${project.period.from} - ${project.period.to})</p>
                <p>${project.description}</p>
                <p>Skills:
                    <#list project.skills as skill>
                        ${skill.title}<#sep>, </#sep>
                    </#list>
                </p>
            </#list>
        </#list>
    </div>

    <!-- Education -->
    <div class="section">
        <h2>Education</h2>
        <#list cv.educations as education>
            <p><strong>${education.title}</strong></p>
            <p>${education.institution} (${education.period.from} - ${education.period.to})</p>
        </#list>
    </div>
</body>
</html>