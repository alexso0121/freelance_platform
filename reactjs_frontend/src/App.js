import logo from './logo.svg';
import './App.css';
import Alert from 'react-bootstrap/Alert';

function App() {
  return (
    <> {[
      'primary',
      'secondary',
      'success',
      'danger',
      'warning',
      'info',
      'light',
      'dark',
    ].map((variant) => (
      <Alert id='Alert' key={variant} variant={variant}>
        This is a {variant} alert—check it out!
      </Alert>
    ))} </>
  );
     
}

export default App;
