const app = Vue.createApp({ //crea una instancia de Vue utilizando metodo createApp y se lo asigna a la constante app (nombre d plantilla de interfaz de usuario)
//para instancia utilizo la libreria instalada en el script

//CICLO DE VIDA diferentes momentos o etapas en los que ocurren eventos y acciones durante la vida útil de la aplicación.
//Aca BEFORE CREATED, dp de que se crea una nueva instancia de VUE pero antes de que se hayan creado las propiedades reactivas y se haya
//compilado la plantilla

//defino el estado de la aplicacion: variables de estado de mi app, que se conocen como propiedades reactivas, cuando cambian sus valores la
//plantilla se actualiza automaticamente con {{}} puedo mostrar esos valores
    data() {
      return {
        firstName: '',
        lastName: '',
        accounts: [],
        hidden:[],
        clients: [],
        loans: [],
        createdAccount: false,
        condicion: true,
        accountType: '',
        formVisible: false,
        mostrarFormulario1: true,
      
        mostrarFormulario2: true,
        selectedAccount: null,
        accNumber: '',
        dateIni: '',
        dateEnd: '',
        checked: '',
        searchClient: ''
      };
    },
    created() { //se ejecuta justo despues de que se hayan creado las propiedades reactivas y compilado la plantilla
      // momento adecuado para realizar operaciones asíncronas, como hacer peticiones a APIs
      this.loadData();
    },
    methods: {
      loadData() {
        axios.get("/api/clients/current")
          .then(response => {
            this.clients = response.data;
            this.accounts = this.clients.accounts;
            console.log(this.accounts)
            this.loans = this.clients.loans;
            this.condicion = this.accounts.length <= 3;
          })
          .catch(err => console.log(err));
      },
      
        getVisibleAccounts() {
          return this.accounts.filter(account => !account.hidden);
        },

      logout() {
        axios.post('/api/logout')
          .then(response => console.log('Signed out'));
      },
      newAccount() {
        Swal.fire({
          icon: 'warning',
          title: 'You are creating a new Account..¿Are you sure?',
          showCancelButton: true,
          confirmButtonText: 'Yes, create new Account',
          cancelButtonText: 'Cancel',
          confirmButtonColor: "#7116d8",
          cancelButtonColor: "#f44336",
          timer: 6000,
          background: '#333',
          textColor: '#FFFFFF',

        }).then((result) => {
          if (result.isConfirmed) {
            axios.post('/api/clients/current/accounts', `accountType=${this.accountType}`)
              .then(response => {
                if (response.status === "201") {
                  this.createdAccount = true;
                  this.loadData();
                  Swal.fire({
                    icon: 'success',
                    title: 'You have a new Account!',
                    showCancelButton: true,
                    confirmButtonText: 'Accepted',
                    cancelButtonText: 'Cancel',
                    timer: 6000,
                  })
                }
              }).then( () => window.location.href="/web/accounts.html")
              .catch(error => Swal.fire({
                icon: 'error',
                title: 'Error',
                text: error.response.data,
                timer: 6000,
                confirmButtonColor: "#7116d8",
                cancelButtonColor: "#f44336",
                timer: 6000,
                background: '#333',
                textColor: '#FFFFFF',
              }));
          }
        });
      },
      showForm() {
        this.formVisible = true;
        this.mostrarFormulario1 = false;
      },

      storeAndShowForm(accountNumber) {
        this.store(accountNumber);
        this.selectedAccount = accountNumber;
   
      },
      generatePDF(accNumber, dateIni, dateEnd) {
        if (this.dateIni !== "" && this.dateEnd !== "") {
          axios.post("/api/clients/current/export-pdf", `accNumber=${this.accNumber}&dateIni=${this.dateIni} 00:00&dateEnd=${this.dateEnd} 23:55`, {
              responseType: 'blob'
            })
            .then((response) => {
              const url = window.URL.createObjectURL(new Blob([response.data], {
                type: "application/pdf"
              }));
              const link = document.createElement('a');
              link.href = url;
              link.setAttribute('download', `Transaction${this.accNumber}.pdf`);
              document.body.appendChild(link);
              link.click();
            })
            .catch(err => console.log(err));
        } else {
          Swal.fire(
            'Select two dates to filter!',
            'warning'
          );
        }
      },
      store(accountNumber) {
        this.accNumber = accountNumber;
        return this.accNumber;
      },
    },
  });
  //inicializo la instancia de Vue
  app.mount('#app');