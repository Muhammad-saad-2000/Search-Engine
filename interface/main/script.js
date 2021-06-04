$("#fuente").on( "click", function() {

      $("#ocultar").toggle();
});

document.querySelector("#buscar").onclick = async () => {//click search
      if (document.querySelector("#search").value == "") {
            alert("Input can't be empty");
      }
      else {
            dataToSend = { word: document.querySelector("#search").value };
            let response = await fetch("http://localhost:8000/word", {
                  method: "POST",
                  body: JSON.stringify(dataToSend),
                  headers: {
                        "Content-Type": "application/json",
                  },
            });
            let resivedData = await response.json();
            if (resivedData.status == 1) {
                  window.location.replace("http://localhost:8000/results/index.html");
            }
      }
}