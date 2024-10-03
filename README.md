This project aims to create a Java-based backend application that uses RAG(Retrieval-Augmented Generation) which is
going to change the way students engage with class materials. The application will use AI technology to assist students in
understanding lecture topics and preparing for exams. The primary goal is to build an eDicient and secure system where
students can upload lecture slides in PDF format, querry information from the lecture slide using AI, and automatically
generate exam preparation materials, such as Multiple Choice Questions (MCQs) and short-answer questions.
2. Technologies Used
• Programming Language: Java
• Framework: Spring Boot
• Database: MySQL
• Security: JWT (JSON Web Tokens) for secure login and logout
• AI Model: Ollama(Llama-3.2) open-source.
• Library: Apache PDFbox, Langchain4j (an open-source Java library for Retrieval-Augmented Generation (RAG)
operations)
• User Interface: Swagger UI
3. Features
3.1 Class Management
• Create Classes: Students can create classes and manage their class materials.
• Upload Materials: Students can upload lecture slides in PDF format. These slides will be stored and processed
for further AI-driven analysis.
3.2 AI-Powered Study Assistance
• Text Extraction and Segmentation: Uploaded PDFs are converted into text documents. The text is segmented to
break down complex topics into manageable sections.
• Vector Embeddings: Segments of text are converted into vector embeddings using Langchain4j. These
embeddings are stored in a inmamory embedding store, which enables eDicient retrieval.
• Retrieval-Augmented Generation (RAG): Based on a student's query, the system retrieves the
most relevant sections of the uploaded lecture slides and provides answers to their queries through the AI model.
The model is capable of handling complex questions, enabling a better understanding of the topics.
3.3Exam Preparation Assistance
• Automatic Summarization: After uploading a PDF, the system generates an automatic summary of the lecture
materials.
• MCQ and Short Answer Generation: The AI system generates potential MCQs and short-answer questions
based on the content of the slides, helping students prepare eDectively for exams.
3.4 User Authentication
• Login and Logout: Students can securely log in and out of the system using JWT tokens. The authentication
system will ensure that user sessions are properly managed, and unauthorized access is restricted.
3.5 API Documentation with Swagger UI
• The project will integrate Swagger UI to automatically generate API documentation. This user interface will allow
developers and users to explore the available endpoints, try out the API, and view real-time responses directly
from the system.
4. Implementation Details
• Spring Boot: A robust Java framework for building REST APIs, it will handle the core logic, including routing,
authentication, and backend services.
• Langchain4j for RAG: Langchain4j will manage the transformation of text segments into embeddings, the
retrieval process, and the integration with AI model Llama-3.2.
• MySQL: A relational database will store user data, class information, lecture materials, and AI-generated
content.
• JWT: JSON Web Tokens will secure the API by managing user authentication and authorization.
• Swagger UI: Swagger will provide a user-friendly interface for testing and understanding the API.
