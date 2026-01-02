import React, { useEffect, useState } from 'react';
import axios from '../../api/axiosConfig';
import { useNavigate } from 'react-router-dom';

const UserList = () => {
    const [users, setUsers] = useState([]);
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [search, setSearch] = useState('');
    const [sortBy, setSortBy] = useState('name');
    const [direction, setDirection] = useState('asc');

    const navigate = useNavigate();

    const fetchUsers = async () => {
        try {
            const response = await axios.get('/admin/users', {
                params: { page, size: 10, sortBy, direction, search }
            });
            setUsers(response.data.content);
            setTotalPages(response.data.totalPages);
        } catch (error) {
            console.error(error);
            if (error.response && error.response.status === 403) {
                alert("Unauthorized");
                navigate('/dashboard');
            }
        }
    };

    useEffect(() => {
        const delayDebounceFn = setTimeout(() => {
            fetchUsers();
        }, 300); // 300ms debounce
        return () => clearTimeout(delayDebounceFn);
    }, [page, sortBy, direction, search]);

    const handleSearch = (e) => {
        setSearch(e.target.value);
        setPage(0); // Reset to page 0 on search
    }

    const handleDelete = async (id) => {
        if (window.confirm('Are you sure you want to delete this user?')) {
            try {
                await axios.delete(`/admin/users/${id}`);
                fetchUsers();
            } catch (e) {
                console.error(e);
                alert("Failed to delete user");
            }
        }
    }

    return (
        <div className="card shadow-sm">
            <div className="card-body">
                <div className="d-flex justify-content-between mb-3 align-items-center">
                    <div className="w-50">
                        <input
                            type="text"
                            className="form-control"
                            placeholder="Search by name or email..."
                            value={search}
                            onChange={handleSearch}
                        />
                    </div>
                    <button className="btn btn-success" onClick={() => navigate('/admin/users/new')}> + Add User</button>
                </div>

                <div className="table-responsive">
                    <table className="table table-hover table-striped">
                        <thead className="table-light">
                            <tr>
                                <th onClick={() => { setSortBy('name'); setDirection(direction === 'asc' ? 'desc' : 'asc') }} style={{ cursor: 'pointer' }}>
                                    Name {sortBy === 'name' && (direction === 'asc' ? '↑' : '↓')}
                                </th>
                                <th onClick={() => { setSortBy('email'); setDirection(direction === 'asc' ? 'desc' : 'asc') }} style={{ cursor: 'pointer' }}>
                                    Email {sortBy === 'email' && (direction === 'asc' ? '↑' : '↓')}
                                </th>
                                <th>Phone</th>
                                <th>Role</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            {users.length > 0 ? users.map(user => (
                                <tr key={user.id}>
                                    <td>{user.name}</td>
                                    <td>{user.email}</td>
                                    <td>{user.phoneNumber}</td>
                                    <td>
                                        <span className={`badge ${user.role === 'ADMIN' ? 'bg-danger' : 'bg-primary'}`}>
                                            {user.role}
                                        </span>
                                    </td>
                                    <td>
                                        <button className="btn btn-sm btn-outline-info me-2" onClick={() => navigate(`/admin/users/edit/${user.id}`)}>Edit</button>
                                        <button className="btn btn-sm btn-outline-danger" onClick={() => handleDelete(user.id)}>Delete</button>
                                    </td>
                                </tr>
                            )) : (
                                <tr><td colSpan="5" className="text-center">No users found</td></tr>
                            )}
                        </tbody>
                    </table>
                </div>

                {/* Pagination */}
                {totalPages > 1 && (
                    <div className="d-flex justify-content-center mt-3">
                        <button className="btn btn-secondary me-2" disabled={page === 0} onClick={() => setPage(page - 1)}>Previous</button>
                        <span className="align-self-center mx-2">Page {page + 1} of {totalPages}</span>
                        <button className="btn btn-secondary ms-2" disabled={page + 1 >= totalPages} onClick={() => setPage(page + 1)}>Next</button>
                    </div>
                )}
            </div>
        </div>
    );
};

export default UserList;
