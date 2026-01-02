import React, { useEffect, useState } from 'react';
import axios from '../../api/axiosConfig';
import { useNavigate } from 'react-router-dom';

const Dashboard = () => {
    const [quote, setQuote] = useState(null);
    const [user, setUser] = useState({});
    const navigate = useNavigate();

    useEffect(() => {
        const storedUser = JSON.parse(localStorage.getItem('user'));
        if (!storedUser) {
            navigate('/login');
            return;
        }
        setUser(storedUser);
        fetchQuote();
    }, [navigate]);

    const fetchQuote = async () => {
        try {
            const response = await axios.get('/dashboard/quote');
            console.log("Quote fetched:", response.data);
            setQuote(response.data.data);
        } catch (err) {
            console.error("Failed to fetch quote", err);
            setQuote({ content: "Believe in yourself!", author: "Unknown" }); // Fallback
        }
    };

    const handleLogout = () => {
        localStorage.clear();
        navigate('/login');
    };

    return (
        <div>
            <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
                <div className="container-fluid">
                    <a className="navbar-brand" href="#">MyApp</a>
                    <div className="d-flex">
                        <button className="btn btn-outline-light" onClick={handleLogout}>Logout</button>
                    </div>
                </div>
            </nav>
            <div className="container mt-5">
                <div className="jumbotron p-5 bg-light rounded shadow-sm">
                    <h1 className="display-4">Welcome, {user.name}!</h1>
                    <p className="lead">We are glad to have you here.</p>
                    <hr className="my-4" />
                    <div className="card">
                        <div className="card-header">
                            Quote of the Day
                        </div>
                        <div className="card-body">
                            <blockquote className="blockquote mb-0">
                                <p>{quote ? quote.content : "Loading..."}</p>
                                <footer className="blockquote-footer">{quote ? quote.quote : ""}</footer>
                                <footer className="blockquote-footer">{quote ? quote.author : ""}</footer>
                            </blockquote>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Dashboard;
