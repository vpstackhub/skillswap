# SkillSwap Backlog

## Epic E1: User Registration & Authentication
Goal: Allow users to sign up and authenticate into the system.
- A1: User Registration Form (Frontend)
- A2: Connect Registration Form to Backend API
- A3: Backend Registration Endpoint with Password Hashing
- A4: Basic Input Validation (email format, password confirmation)
- A5: Future: Implement Real Database Persistence (Postgres)
- A6: Future: Login Endpoint and JWT-based Authentication

## Epic E2: Environment & Deployment Setup
Goal: Set up the project for local development and deployment.
- A1: Create Monorepo Structure with Frontend + Backend
- A2: Setup `.gitignore` and Clean Initial Commit
- A3: Create GitHub Repository and Push Initial Code
- A4: Setup Environment Files (environment.ts / environment.prod.ts)
- A5: Future: Dockerize Backend and Frontend
- A6: Future: CI/CD Pipeline with Jenkins and AWS EC2

## Epic E3: User Experience & Validation
Goal: Improve user interaction and form feedback.
- A1: Enhance Registration Page (UI/UX improvements)
- A2: Add Real-Time Email Validation (duplicate check)
- A3: Add Password Strength Indicator
- A4: Add Error/Success Notifications

## Epic E4: Database & Data Model
Goal: Transition from in-memory to a persistent database with proper schema.
- A1: Set Up Postgres Database
- A2: Create User Table with Unique Email Constraint
- A3: Integrate Backend with Postgres via JPA
- A4: Seed Data for Testing
- A5: Add Email Existence Check in UserService

## Epic E5: Collaboration & Documentation
Goal: Make the project easy to collaborate on and maintain.
- A1: Create README with Project Overview
- A2: Create Backlog Document (this file)
- A3: Invite Collaborator (Sameer)
- A4: Document Setup Instructions for New Developers
- A5: Document API Endpoints and Sample Payloads
