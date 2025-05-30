🌟 STUDENT ATTENDANCE MANAGEMENT SYSTEM 🌟
=========================================

📌 OVERVIEW:
-------------
🎓 This is a **Java-based Desktop Application** that allows you to manage student records and their attendance efficiently.

🔧 Built using:
   ✔️ 100% Java
   ✔️ Swing (Java GUI)
   ✔️ MySQL (Database)
   ✔️ JDBC (Database Connectivity)

🖥️ MAIN WINDOW FEATURES:
------------------------
On launching the app, you’ll see **three main buttons**:

1️⃣ ➤ **Add Student**
   🧍 Enter:
   - Student Name
   - Standard (Class)
   💾 Data is saved directly into the MySQL database.

2️⃣ ➤ **Add Attendance**
   📅 Choose between:
   - ✅ **Single Attendance** → Mark presence for an individual student on a specific date.
   - 📆 **Monthly Attendance** → Record attendance for the entire month for a student.

3️⃣ ➤ **View Attendance**
   👀 Filter by:
   - Standard
   - Month
   - Year
   📊 Displays a detailed attendance report.

🗄️ DATABASE STRUCTURE:
-----------------------
🧾 Table: `students`
   - 📌 `id` (INT, Primary Key, Auto Increment)
   - 🧑 `name` (VARCHAR)
   - 🏫 `standard` (VARCHAR)

🧾 Table: `attendance`
   - 📌 `id` (INT, Primary Key)
   - 🧑 `student_id` (INT, Foreign Key)
   - 📅 `attendance date` (DATE)
   - 📍 `status` (ENUM – 'A', 'P', 'H', 'W')

🚀 HOW TO RUN:
--------------
1. 📥 Set up MySQL and create the above tables.
2. 🛠️ Update JDBC config in the code (`url`, `username`, `password`).
3. 🖱️ Run the main Java class (e.g., `Main.java` or `AttendanceManager.java`).
4. 🎉 Use the GUI to:
   - Add Students
   - Record Attendance
   - View Reports

📦 REQUIREMENTS:
----------------
🧰 Java JDK 8+
🐬 MySQL Server
🔗 MySQL JDBC Driver (Connector/J)

📈 FUTURE UPGRADES (Ideas 💡):
-----------------------------
✨ User Login & Roles (Admin/Teacher)
📤 Export Reports to PDF/Excel
📊 Visual Charts for Attendance Analytics
🔔 Absence Notifications

👨‍💻 AUTHOR:
-----------
💡 Sanika Thakur 
🌐 GitHub: [TheSanikaT]  
📧 Email: sanikathakur39@gmail.com

📝 License: MIT 

✨ Happy Coding! ✨
