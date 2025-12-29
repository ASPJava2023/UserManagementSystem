import React, { useState } from 'react';
import axios from '../../api/axiosConfig';
import { useNavigate, useLocation } from 'react-router-dom';

const ResetPassword = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const [form, setForm] = useState({
        email: location.state?.email || '',
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
    });
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');

    const handleChange = (e) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        if (form.newPassword !== form.confirmPassword) {
            setError("New passwords don't match");
            return;
        }

        try {
            await axios.post('/auth/reset-password', form);
            setSuccess('Password reset successful. Please login again.');
            localStorage.clear(); // Clear local storage to force relogin
            setTimeout(() => navigate('/login'), 2000);
        } catch (err) {
            setError(err.response?.data?.message || 'Password reset failed');
        }
    };

    return (
        <div className="container mt-5">
            <div className="row justify-content-center">
                <div className="col-md-5">
                    <div className="card shadow">
                        <div className="card-body">
                            <h2 className="text-center mb-4">Reset Password</h2>
                            {error && <div className="alert alert-danger">{error}</div>}
                            {success && <div className="alert alert-success">{success}</div>}
                            <form onSubmit={handleSubmit}>
                                <div className="mb-3">
                                    <label className="form-label">Email</label>
                                    <input type="email" name="email" className="form-control" value={form.email} onChange={handleChange} required readOnly={!!location.state?.email} />
                                </div>
                                <div className="mb-3">
                                    <label className="form-label">Old Password (Temporary)</label>
                                    <input type="password" name="oldPassword" className="form-control" onChange={handleChange} required />
                                </div>
                                <div className="mb-3">
                                    <label className="form-label">New Password</label>
                                    <input type="password" name="newPassword" className="form-control" onChange={handleChange} required />
                                </div>
                                <div className="mb-3">
                                    <label className="form-label">Confirm New Password</label>
                                    <input type="password" name="confirmPassword" className="form-control" onChange={handleChange} required />
                                </div>
                                <button type="submit" className="btn btn-warning w-100">Reset Password</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default ResetPassword;
