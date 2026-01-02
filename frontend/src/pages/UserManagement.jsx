import React from 'react';
import UserList from '../components/admin/UserList';
import { Link } from 'react-router-dom';

const UserManagement = () => {
    return (
        <div className="container mt-5">
            <nav aria-label="breadcrumb">
                <ol className="breadcrumb">
                    <li className="breadcrumb-item"><Link to="/admin/dashboard">Admin Dashboard</Link></li>
                    <li className="breadcrumb-item active" aria-current="page">User Management</li>
                </ol>
            </nav>
            <h2 className="mb-4">User Management</h2>
            <UserList />
        </div>
    );
};
export default UserManagement;
