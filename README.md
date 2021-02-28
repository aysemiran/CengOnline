# CengOnline

## Course Management Application with Android Studio 

#### Scope of the Project


It is expected to create a course management system in this project. In this system, which has 2
types of user types, students will have the authority to follow the information related to their
courses, to see assignments and announcements, to upload assignments, to follow posts in the
stream, to comment a post in the stream and send or receive messages. Teachers will have the
authority to follow progress related to their courses, view, add, edit and delete assignments and
announcements, view assignments uploaded by the student, follow posts in the stream, add new
posts to the stream, edit and delete posts, to comment a post in the stream and send or receive
messages.

##### Functionalities
###### Login
Two types of users can log into the CENGOnline course management system: Teachers and
Students.
######  Courses
Teachers can add/edit/delete a course. Students can only view the courses they have enrolled
in.
###### Assignments
Teachers can add/edit/delete an assignment and can view submitted works by students.
Students can only view the assignments and upload their works (only text submission, no file
upload required).
###### Announcements
Teachers can add/edit/delete announcements. Students can only view the announcements.
###### Messaging
All the system users can send individual messages to each other.
###### Stream
Each course has an own stream. Participants can communicate on the stream page using posts
and comments. Teachers can add/edit/delete posts. Teachers and students can send comments
as response to a post.

### Requirements
- Application is implemented in Java programming languages.
- It is designed the system in accordance with Object Oriented Programming.
- It is developed the project on Android Studio 
- It is designed the UI in mobile form.
- It is used the Firebase to data storage.
- These following programming concepts are implemented at least one time in the project.
o Inheritance
o Abstract data type
o Foreach loop
o Switch-case condition
o Named constants
o Associative Arrays
o Method Overloading
- ✨

## Installation and Implementation 

The project is developed  with Java in Android Studio.
Android Studio programs must be installed on your computer.

>https://developer.android.com/studio

You have to connect your project to your database in firebase.
>https://console.firebase.google.com/

The form of a tree structure in the database is as follows.
- Courses
    - c1
        - announcements
            - announcement1
                - announcementContext
                 - announcementID
                - publishedDate
            - assignments
                - assignment1
                    - Submitters
                        - Submit1
                            - AssignmentContext
                            - Date
                    - assignmentContext
                    - assignmentID
                    - assignmentName
                    - publishedDate
          - courseID
           - courseName
          - lectureGiven
            - numOfStudentsTaken
            - stream    
                - comments
                    - comment1
                       - commentContext
                        - commentDate
                        - whoShared
                - streamContext
                - streamDate
                - streamID
                - whoShared


- User
    - 101
        - ID
        - courses
            - c1
        - department
        - messages
            - message1
            - date
            - isRead
            - messageContext
            - receiver
            - sender
        - name
        - numberOfCourses
        - password
        - userType
        - username

