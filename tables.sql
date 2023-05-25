--جدول المساقات
CREATE TABLE managment.courses (
  course_code VARCHAR(10) PRIMARY KEY,
  course_name VARCHAR(100) NOT null UNIQUE,
  subject VARCHAR(50),
  book VARCHAR(100),
  teacher VARCHAR(100),
  place VARCHAR(50)
);
--جدول الطلاب
CREATE TABLE managment.students (
  student_number VARCHAR(20) PRIMARY KEY,
  full_name VARCHAR(100) NOT null UNIQUE,
  mobile varchar(10),
  living VARCHAR(50),
  gender VARCHAR(10),
  department VARCHAR(50),
  major VARCHAR(50)
);
-- جدول المستخدمين
create table managment.users (
  user_id VARCHAR(20) primary key,
  full_name VARCHAR(100) not null unique,
  living VARCHAR(50),
  department VARCHAR(50),
  mobile VARCHAR(20) unique,
  password VARCHAR(100),
  is_admin varchar(1)
);

--جدول المحاضرات
CREATE TABLE managment.lectures (
  id SERIAL PRIMARY KEY,
  course_code VARCHAR(20) NOT NULL,
  title VARCHAR(100) NOT NULL,
  place VARCHAR(50),
  day VARCHAR(10),
  date timestamp,
  FOREIGN KEY (course_code) REFERENCES courses (course_code)
);

--جدول الحضور و الغياب
CREATE TABLE managment.attendance (
  id SERIAL PRIMARY KEY,
  lecture_id INTEGER,
  student_number VARCHAR(20),
  student_name VARCHAR(100),
  student_mobile VARCHAR(20),
  status VARCHAR(20),
  FOREIGN KEY (lecture_id) REFERENCES lectures (id),
  FOREIGN KEY (student_number) REFERENCES students (student_number)
);

--جدول مساقات الطلاب
CREATE TABLE managment.students_courses (
  id SERIAL PRIMARY KEY,
  student_number VARCHAR(20),
  course_code VARCHAR(20),
  status VARCHAR(20),
  FOREIGN KEY (student_number) REFERENCES students (student_number),
  FOREIGN KEY (course_code) REFERENCES courses (course_code)
);
--حدول مساقات المعيدين
create table managment.user_courses (
  id SERIAL primary key,
  user_id VARCHAR(20),
  code VARCHAR(20),
  foreign key (user_id) references users (user_id),
  foreign key (code) references courses (course_code)
);


ALTER TABLE managment.user_courses ADD CONSTRAINT assistants_courses_un UNIQUE (user_id,code);
ALTER TABLE managment.lectures ALTER COLUMN "date" TYPE date USING "date"::date;
ALTER TABLE managment.attendance ADD CONSTRAINT attendance_un UNIQUE (lecture_id,student_number);
ALTER TABLE managment.users ALTER COLUMN is_admin SET DEFAULT 0;
ALTER TABLE managment.user_courses DROP CONSTRAINT user_courses_user_id_fkey;
ALTER TABLE managment.user_courses ADD CONSTRAINT user_courses_fk FOREIGN KEY (user_id) REFERENCES managment.users(user_id);
ALTER TABLE managment.user_courses DROP CONSTRAINT user_courses_code_fkey;
ALTER TABLE managment.user_courses ADD CONSTRAINT user_courses_fk2 FOREIGN KEY (code) REFERENCES managment.courses(course_code);
ALTER TABLE managment.lectures DROP CONSTRAINT lectures_course_code_fkey;
ALTER TABLE managment.lectures ADD CONSTRAINT lectures_fk FOREIGN KEY (course_code) REFERENCES managment.courses(course_code);
ALTER TABLE managment.students_courses DROP CONSTRAINT students_courses_student_number_fkey;
ALTER TABLE managment.students_courses DROP CONSTRAINT students_courses_course_code_fkey;
ALTER TABLE managment.students_courses ADD CONSTRAINT students_courses_fk FOREIGN KEY (student_number) REFERENCES managment.students(student_number);
ALTER TABLE managment.students_courses ADD CONSTRAINT students_courses_fk2 FOREIGN KEY (course_code) REFERENCES managment.courses(course_code);
ALTER TABLE managment.attendance DROP CONSTRAINT attendance_lecture_id_fkey;
ALTER TABLE managment.attendance DROP CONSTRAINT attendance_student_number_fkey;
ALTER TABLE managment.attendance ADD CONSTRAINT attendance_fk FOREIGN KEY (lecture_id) REFERENCES managment.lectures(id);
ALTER TABLE managment.attendance ADD CONSTRAINT attendance_fk2 FOREIGN KEY (student_number) REFERENCES managment.students(student_number);























