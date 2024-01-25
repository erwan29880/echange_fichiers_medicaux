try {
    const div = document.querySelector("#url");
    const ahref = document.querySelector("#ahref");
    
    ahref.addEventListener("click", () => {
        div.innerHTML = "Le fichier sera supprimé dans une minute.";
        
        setTimeout(() => {
            fetch("/delete", {
                method: "DELETE"
            }).then(res => res.json())
            .then(res => console.log("success"))
            .catch(err => console.log("erreur serveur"));
        }, 500);
    
    
    })
} catch (err) {
    
}

