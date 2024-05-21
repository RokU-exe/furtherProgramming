<!DOCTYPE html>
<html>
<head>
</head>
<body>

<h1>Insurance Claims Management System</h1>

<h2>Introduction</h2>

<p>This system streamlines insurance claim management, providing features for customers, providers, and administrators to file, process, and track claims. It's built with JavaFX for the interface and leverages PostgreSQL (hosted on Supabase or Neon) for secure, real-time data management.</p>

<h2>System Setup</h2>

<h3>Prerequisites</h3>

<ul>
<li>Java Development Kit (JDK) 11 or later</li>
<li>PostgreSQL database (Supabase or Neon recommended)</li>
<li>JavaFX libraries (included in most JDK distributions)</li>
<li>Supabase or Neon account (for database hosting)</li>
</ul>

<h3>Database Setup</h3>

<ol>
<li>Create a Supabase or Neon project and set up your PostgreSQL database.</li>
<li>Execute the provided SQL scripts to create tables and load initial data. (See <code>database_setup.sql</code>)</li>
<li>Update the <code>database.properties</code> file with your database credentials.</li>
</ol>

<h3>Building and Running</h3>

<ol>
<li>Open the project in your favorite Java IDE (e.g., IntelliJ IDEA, Eclipse).</li>
<li>Ensure the JavaFX libraries are added to your project's build path.</li>
<li>Build the project (e.g., <code>mvn clean install</code> if using Maven).</li>
<li>Run the <code>Main</code> class to start the application.</li>
</ol>

<h2>User Interface (JavaFX)</h2>

<h3>Login</h3>
<p>Upon launching, you'll see a login screen. Enter your credentials based on your role:</p>

<h3>Role-Specific Actions</h3>

<ul>
<li><b>Policy Holders:</b> File, update, retrieve claims (own and dependents'), manage personal/dependent information.</li>
<li><b>Dependents:</b> Retrieve own claims and information.</li>
<li><b>Policy Owners:</b> Manage beneficiaries and their claims, calculate insurance premiums.</li>
<li><b>Insurance Surveyors:</b> Request claim details, propose claims to managers, retrieve claims/customer data.</li>
<li><b>Insurance Managers:</b> Approve/reject claims, retrieve claims/customer data, access surveyor information.</li>
<li><b>System Admin:</b> Full CRUD operations (except claim editing), generate claim reports.</li>
</ul>
<h3>Navigating the Interface</h3>

<p>Use the intuitive menus and buttons to perform actions based on your role. The system will guide you through each process.</p>

<h3>Troubleshooting</h3>
<ul>
  <li>For database connection issues, verify your credentials in `database.properties`</li>
  <li>If you are running into a "No suitable driver found" issue, it will probably be related to a database configuration issue. Make sure you are using the correct database driver for the version of the database you are trying to connect to.</li>
  <li> If your IDE cannot find the JavaFX modules, make sure they are added to your module path and your vm options.</li>
</ul>
<h2>Additional Notes</h2>

<ul>
<li>The system supports real-time updates, reflecting changes immediately in the database.</li>
<li>Input validation is in place to ensure data integrity.</li>
<li>Exception handling gracefully manages errors for a smooth user experience.</li>
</ul>
</body>
</html>
