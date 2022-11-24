import Sequelize  from "sequelize";

const sequelize = new Sequelize("auth-db", "postgres", "123", {
  host: "localhost",
  dialect: "postgres",
  quoteIdentifiers: false,
  define: {
    synOnAssociation: true,
    timestamps: false,
    underscored: true,
    underscoredAll: true,
    freezeTableName: true
  }
});

sequelize
  .authenticate()
  .then(() => {
    console.info("Connection stablished!");
  })
  .catch((err) => {
    console.log("Err to connect to the database");
    console.error(err.message);
  });

  export default sequelize;



