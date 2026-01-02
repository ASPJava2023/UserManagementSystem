import React from 'react';
import { Link, useNavigate } from 'react-router-dom';

const AdminDashboard = () => {
    const navigate = useNavigate();

    return (
        <div className="container mt-5">
            <h1 className="mb-4">Admin Dashboard</h1>
            <div className="row">
                <div className="col-md-4">
                    <div className="card shadow-sm">
                        <div className="card-body">
                            <h5 className="card-title">User Management</h5>
                            <p className="card-text">View, add, edit, and delete users. Manage roles.</p>
                            <Link to="/admin/users" className="btn btn-primary">Manage Users</Link>
                        </div>
                    </div>
                </div>
                {/* Placeholder for locations or other admin tasks */}
            </div>
            <div className="mt-4">
                <button className="btn btn-danger" onClick={() => {
                    localStorage.removeItem('token');
                    localStorage.removeItem('user');
                    navigate('/login');
                }}>Logout</button>
            </div>
        </div>
    );
};
export default AdminDashboard;
