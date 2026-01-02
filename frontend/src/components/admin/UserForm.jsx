import React, { useState, useEffect } from 'react';
import axios from '../../api/axiosConfig';
import { useNavigate, useParams } from 'react-router-dom';

const UserForm = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const isEdit = !!id;

    const [countries, setCountries] = useState([]);
    const [states, setStates] = useState([]);
    const [cities, setCities] = useState([]);

    const [form, setForm] = useState({
        name: '',
        email: '',
        phoneNumber: '',
        countryId: '',
        stateId: '',
        cityId: '',
        role: 'USER'
    });

    const [error, setError] = useState('');

    useEffect(() => {
        fetchCountries();
        if (isEdit) {
            fetchUser();
        }
    }, [id]);

    const fetchCountries = async () => {
        try {
            const response = await axios.get('/locations/countries');
            setCountries(response.data.data);
        } catch (err) { console.error(err); }
    };

    const fetchUser = async () => {
        try {
            const response = await axios.get(`/admin/users/${id}`);
            const user = response.data;
            console.log("Fetched user", user);

            // Fetch states and cities for the selected country/state first
            if (user.countryId) await fetchStates(user.countryId);
            if (user.stateId) await fetchCities(user.stateId);

            setForm({
                name: user.name,
                email: user.email,
                phoneNumber: user.phoneNumber,
                countryId: user.countryId || '',
                stateId: user.stateId || '',
                cityId: user.cityId || '',
                role: user.role || 'USER'
            });
        } catch (err) {
            setError('Failed to fetch user data');
            console.error(err);
        }
    };

    const fetchStates = async (countryId) => {
        try {
            const response = await axios.get(`/locations/states/${countryId}`);
            setStates(response.data.data);
        } catch (e) { console.error(e); }
    }

    const fetchCities = async (stateId) => {
        try {
            const response = await axios.get(`/locations/cities/${stateId}`);
            setCities(response.data.data);
        } catch (e) { console.error(e); }
    }

    const handleCountryChange = async (e) => {
        const countryId = e.target.value;
        setForm({ ...form, countryId, stateId: '', cityId: '' });
        setStates([]);
        setCities([]);
        if (countryId) fetchStates(countryId);
    };

    const handleStateChange = async (e) => {
        const stateId = e.target.value;
        setForm({ ...form, stateId, cityId: '' });
        setCities([]);
        if (stateId) fetchCities(stateId);
    };

    const handleChange = (e) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        try {
            if (isEdit) {
                await axios.put(`/admin/users/${id}`, form);
            } else {
                await axios.post('/admin/users', form);
            }
            navigate('/admin/users');
        } catch (err) {
            setError(err.response?.data?.message || 'Operation failed');
        }
    };

    return (
        <div className="container mt-5">
            <div className="card shadow-sm">
                <div className="card-body">
                    <h3 className="mb-4">{isEdit ? 'Edit User' : 'Create User'}</h3>
                    {error && <div className="alert alert-danger">{error}</div>}
                    <form onSubmit={handleSubmit}>
                        <div className="mb-3">
                            <label className="form-label">Name</label>
                            <input type="text" name="name" className="form-control" value={form.name} onChange={handleChange} required />
                        </div>
                        <div className="mb-3">
                            <label className="form-label">Email</label>
                            <input type="email" name="email" className="form-control" value={form.email} onChange={handleChange} required />
                        </div>
                        <div className="mb-3">
                            <label className="form-label">Phone</label>
                            <input type="text" name="phoneNumber" className="form-control" value={form.phoneNumber} onChange={handleChange} />
                        </div>
                        <div className="mb-3">
                            <label className="form-label">Role</label>
                            <select name="role" className="form-select" value={form.role} onChange={handleChange}>
                                <option value="USER">USER</option>
                                <option value="ADMIN">ADMIN</option>
                            </select>
                        </div>

                        <div className="row">
                            <div className="col-md-4 mb-3">
                                <label className="form-label">Country</label>
                                <select name="countryId" className="form-select" value={form.countryId} onChange={handleCountryChange} required>
                                    <option value="">Select Country</option>
                                    {countries.map(c => <option key={c.id} value={c.id}>{c.name}</option>)}
                                </select>
                            </div>
                            <div className="col-md-4 mb-3">
                                <label className="form-label">State</label>
                                <select name="stateId" className="form-select" value={form.stateId} onChange={handleStateChange} required disabled={!states.length}>
                                    <option value="">Select State</option>
                                    {states.map(s => <option key={s.id} value={s.id}>{s.name}</option>)}
                                </select>
                            </div>
                            <div className="col-md-4 mb-3">
                                <label className="form-label">City</label>
                                <select name="cityId" className="form-select" value={form.cityId} onChange={handleChange} required disabled={!cities.length}>
                                    <option value="">Select City</option>
                                    {cities.map(c => <option key={c.id} value={c.id}>{c.name}</option>)}
                                </select>
                            </div>
                        </div>

                        <button type="submit" className="btn btn-primary">{isEdit ? 'Update' : 'Create'}</button>
                        <button type="button" className="btn btn-secondary ms-2" onClick={() => navigate('/admin/users')}>Cancel</button>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default UserForm;
