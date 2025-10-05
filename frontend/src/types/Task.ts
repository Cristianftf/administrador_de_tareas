export interface Task {
  id?: number;
  title: string;
  description: string;
  completed: boolean;
  createdAt?: string; // ISO string format
  reminderAt?: string; // ISO string format
  reminderSent?: boolean;
}

export interface CreateTaskRequest {
  title: string;
  description: string;
  completed: boolean;
}

export interface UpdateTaskRequest {
  title?: string;
  description?: string;
  completed?: boolean;
  reminderAt?: string; // ISO string format
}
