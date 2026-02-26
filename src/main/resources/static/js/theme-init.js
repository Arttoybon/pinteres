(function () {
    const tema = localStorage.getItem('theme') || 'light';
    if (tema === 'dark') {
        document.documentElement.setAttribute('data-bs-theme', 'dark');
    }
})();
