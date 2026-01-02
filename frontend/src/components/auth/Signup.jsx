import React, { useState, useEffect } from 'react';
import axios from '../../api/axiosConfig';
import { useNavigate } from 'react-router-dom';

const Signup = () => {
    const [countries, setCountries] = useState([]);
    const [states, setStates] = useState([]);
    const [cities, setCities] = useState([]);
    const navigate = useNavigate();

    const [form, setForm] = useState({
        name: '',
        email: '',
        phoneNumber: '',
        countryId: '',
        stateId: '',
        cityId: ''
    });

    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');

    useEffect(() => {
        fetchCountries();
    }, []);

    const fetchCountries = async () => {
        try {
            const response = await axios.get('/locations/countries');
            setCountries(response.data.data);
        } catch (err) {
            console.error("Failed to fetch countries", err);
        }
    };

    const handleCountryChange = async (e) => {
        const countryId = e.target.value;
        setForm({ ...form, countryId, stateId: '', cityId: '' });
        setStates([]);
        setCities([]);
        if (countryId) {
            try {
                const response = await axios.get(`/locations/states/${countryId}`);
                setStates(response.data.data);
            } catch (err) {
                console.error("Failed to fetch states", err);
            }
        }
    };

    const handleStateChange = async (e) => {
        const stateId = e.target.value;
        setForm({ ...form, stateId, cityId: '' });
        setCities([]);
        if (stateId) {
            try {
                const response = await axios.get(`/locations/cities/${stateId}`);
                setCities(response.data.data);
            } catch (err) {
                console.error("Failed to fetch cities", err);
            }
        }
    };

    const handleChange = (e) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setSuccess('');
        const payload = {
            ...form,
            countryId: Number(form.countryId),
            stateId: Number(form.stateId),
            cityId: Number(form.cityId)
        };

        try {
            const response = await axios.post('/auth/signup', payload);
            setSuccess('Registration successful! Check your email for login credentials.');
            setTimeout(() => navigate('/login'), 3000);
        } catch (err) {
            setError(err.response?.data?.message || 'Registration failed');
        }
    };

    return (
        <div className="container mt-5">
            <div className="row justify-content-center">
                <div className="col-md-6">
                    <div className="card shadow">
                        <div className="card-body">
                            <h2 className="text-center mb-4">Sign Up</h2>
                            {error && <div className="alert alert-danger">{error}</div>}
                            {success && <div className="alert alert-success">{success}</div>}
                            <form onSubmit={handleSubmit}>
                                <div className="mb-3">
                                    <label className="form-label">Name</label>
                                    <input type="text" name="name" className="form-control" onChange={handleChange} required />
                                </div>
                                <div className="mb-3">
                                    <label className="form-label">Email</label>
                                    <input type="email" name="email" className="form-control" onChange={handleChange} required />
                                </div>
                                <div className="mb-3">
                                    <label className="form-label">Phone Number</label>
                                    <input type="text" name="phoneNumber" className="form-control" onChange={handleChange} />
                                </div>
                                <div className="mb-3">
                                    <label className="form-label">Country</label>
                                    <select name="countryId" className="form-select" onChange={handleCountryChange} required>
                                        <option value="">Select Country</option>
                                        {countries.map(c => <option key={c.id} value={c.id}>{c.name}</option>)}
                                    </select>
                                </div>
                                <div className="mb-3">
                                    <label className="form-label">State</label>
                                    <select name="stateId" className="form-select" onChange={handleStateChange} required disabled={!states.length}>
                                        <option value="">Select State</option>
                                        {states.map(s => <option key={s.id} value={s.id}>{s.name}</option>)}
                                    </select>
                                </div>
                                <div className="mb-3">
                                    <label className="form-label">City</label>
                                    <select name="cityId" className="form-select" onChange={handleChange} required disabled={!cities.length}>
                                        <option value="">Select City</option>
                                        {cities.map(c => <option key={c.id} value={c.id}>{c.name}</option>)}
                                    </select>
                                </div>
                                <button type="submit" className="btn btn-primary w-100">Register</button>
                            </form>
                            <div className="mt-3 text-center">
                                <a href="/login">Already have an account? Login</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Signup;
