import React, { useEffect, useRef, useState } from 'react';

function Home() {
    const [list, setList] = useState([]);

    const [listening, setListening] = useState(false);
    const [transcript, setTranscript] = useState('');
    const recognitionRef = useRef(null);

    const handleStart = () => {
        const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;

        if (!SpeechRecognition) {
            alert('Your browser does not support Speech Recognition');
            return;
        }

        const recognition = new SpeechRecognition();
        recognition.continuous = true;
        recognition.interimResults = true;
        recognition.lang = 'en-US';

        let finalTranscript = '';

        recognition.onresult = (event) => {
            for (let i = event.resultIndex; i < event.results.length; i++) {
                finalTranscript += event.results[i][0].transcript;
            }
            setTranscript(finalTranscript);
        };

        recognition.onerror = (event) => {
            console.error('Speech recognition error:', event.error);
        };

        recognition.onend = () => {
            setListening(false);
            console.log("Final Sentence:", finalTranscript); // ✅ Now it shows correctly
        };

        recognition.start();
        recognitionRef.current = recognition;
        setListening(true);
    };



    const handleStop = () => {
        if (recognitionRef.current) {
            recognitionRef.current.stop();
        }
        setListening(false);
    };

    const handleReset = () => {
        setTranscript('');
    };

    const getmenuItems = async () => {
        const response = await fetch('http://localhost:8080/api/user/get-all-items');

        const data = await response.json();

        setList(data.data)
        console.log("Menu", data);
    };

    useEffect(() => {
        getmenuItems();
    }, []);

    return (
        <div><div className="list"> <h1>Menu Items</h1>
            <ul>
                {list.map(item => (
                    <li key={item.id}>
                        <strong>{item.itemName}</strong> — ₹{item.itemPrice} —
                        {item.status ? ' ✅ Available' : ' ❌ Not Available'}
                    </li>
                ))}
            </ul></div>
            <div className="mic"> <div style={{ padding: '30px', fontFamily: 'sans-serif' }}>
                <h1>🎤 Voice to Text (React)</h1>
                <p>Status: {listening ? 'Listening...' : 'Click "Start Listening"'}</p>
                <button onClick={handleStart} disabled={listening}>Start Listening</button>
                <button onClick={handleStop} disabled={!listening}>Stop</button>
                <button onClick={handleReset}>Reset</button>

                <h3>Transcript:</h3>
                <div style={{
                    backgroundColor: '#f0f0f0',
                    padding: '15px',
                    minHeight: '100px',
                    borderRadius: '5px',
                    marginTop: '10px'
                }}>
                    {transcript || 'Speak something...'}
                </div>
            </div></div>
        </div>
    );
}

export default Home;
