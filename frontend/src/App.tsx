import React, { useState, useEffect } from 'react';
import { Task, CreateTaskRequest } from './types/Task';
import taskApi from './services/taskApi';
import './App.css';

function App() {
  const [tasks, setTasks] = useState<Task[]>([]);
  const [newTask, setNewTask] = useState<CreateTaskRequest>({
    title: '',
    description: '',
    completed: false
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    loadTasks();
  }, []);

  const loadTasks = async () => {
    try {
      setLoading(true);
      const tasksData = await taskApi.getAllTasks();
      setTasks(tasksData);
      setError(null);
    } catch (err) {
      setError('Error al cargar las tareas');
      console.error('Error loading tasks:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateTask = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!newTask.title.trim()) return;

    try {
      const createdTask = await taskApi.createTask(newTask);
      setTasks([...tasks, createdTask]);
      setNewTask({ title: '', description: '', completed: false });
      setError(null);
    } catch (err) {
      setError('Error al crear la tarea');
      console.error('Error creating task:', err);
    }
  };

  const handleUpdateTask = async (id: number, updates: Partial<Task>) => {
    try {
      const updatedTask = await taskApi.updateTask(id, updates);
      setTasks(tasks.map(task => task.id === id ? updatedTask : task));
      setError(null);
    } catch (err) {
      setError('Error al actualizar la tarea');
      console.error('Error updating task:', err);
    }
  };

  const handleDeleteTask = async (id: number) => {
    try {
      await taskApi.deleteTask(id);
      setTasks(tasks.filter(task => task.id !== id));
      setError(null);
    } catch (err) {
      setError('Error al eliminar la tarea');
      console.error('Error deleting task:', err);
    }
  };

  const handleToggleComplete = async (task: Task) => {
    await handleUpdateTask(task.id!, { completed: !task.completed });
  };

  const formatDate = (dateString?: string) => {
    if (!dateString) return 'N/A';
    return new Date(dateString).toLocaleString();
  };

  if (loading) {
    return <div className="app-loading">Cargando...</div>;
  }

  return (
    <div className="app">
      <header className="app-header">
        <h1>Administrador de Tareas</h1>
      </header>

      <main className="app-main">
        {error && (
          <div className="error-message">
            {error}
            <button onClick={() => setError(null)}>×</button>
          </div>
        )}

        {/* Formulario para crear nueva tarea */}
        <form onSubmit={handleCreateTask} className="task-form">
          <h3>Nueva Tarea</h3>
          <div className="form-group">
            <input
              type="text"
              placeholder="Título de la tarea"
              value={newTask.title}
              onChange={(e) => setNewTask({ ...newTask, title: e.target.value })}
              required
            />
          </div>
          <div className="form-group">
            <textarea
              placeholder="Descripción"
              value={newTask.description}
              onChange={(e) => setNewTask({ ...newTask, description: e.target.value })}
              rows={3}
            />
          </div>
          <button type="submit" className="btn-primary">
            Crear Tarea
          </button>
        </form>

        {/* Lista de tareas */}
        <div className="tasks-list">
          <h3>Tareas ({tasks.length})</h3>
          {tasks.length === 0 ? (
            <p className="no-tasks">No hay tareas creadas</p>
          ) : (
            tasks.map((task) => (
              <div key={task.id} className={`task-item ${task.completed ? 'completed' : ''}`}>
                <div className="task-content">
                  <h4>{task.title}</h4>
                  {task.description && <p>{task.description}</p>}
                  <div className="task-meta">
                    <span>Creada: {formatDate(task.createdAt)}</span>
                    {task.reminderAt && (
                      <span>Recordatorio: {formatDate(task.reminderAt)}</span>
                    )}
                  </div>
                </div>
                <div className="task-actions">
                  <button
                    onClick={() => handleToggleComplete(task)}
                    className={`btn-toggle ${task.completed ? 'completed' : ''}`}
                  >
                    {task.completed ? '✓ Completada' : 'Marcar como completada'}
                  </button>
                  <button
                    onClick={() => handleDeleteTask(task.id!)}
                    className="btn-delete"
                  >
                    Eliminar
                  </button>
                </div>
              </div>
            ))
          )}
        </div>
      </main>
    </div>
  );
}

export default App;
