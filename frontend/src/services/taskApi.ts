import axios from 'axios';
import { Task, CreateTaskRequest, UpdateTaskRequest } from '../types/Task';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const taskApi = {
  // Get all tasks
  getAllTasks: async (): Promise<Task[]> => {
    const response = await api.get<Task[]>('/tasks');
    return response.data;
  },

  // Get a single task by ID
  getTask: async (id: number): Promise<Task> => {
    const response = await api.get<Task>(`/tasks/${id}`);
    return response.data;
  },

  // Create a new task
  createTask: async (task: CreateTaskRequest): Promise<Task> => {
    const response = await api.post<Task>('/tasks', task);
    return response.data;
  },

  // Update an existing task
  updateTask: async (id: number, task: UpdateTaskRequest): Promise<Task> => {
    const response = await api.put<Task>(`/tasks/${id}`, task);
    return response.data;
  },

  // Delete a task
  deleteTask: async (id: number): Promise<void> => {
    await api.delete(`/tasks/${id}`);
  },

  // Set reminder for a task
  setReminder: async (id: number, reminderAt: string): Promise<Task> => {
    const response = await api.put<Task>(`/tasks/${id}/reminder?at=${encodeURIComponent(reminderAt)}`);
    return response.data;
  },
};

export default taskApi;
