import bcrypt from "bcrypt";
import User from "../../modules/user/model/User.js";

export const teste = "1";

export async function createInitialData(){
  try {
    await User.sync({force: true});

    let password = await bcrypt.hash('123456', 10);
  
     await User.create({
      name: 'User test',
      email: 'teste@user.com',
      password,
    });

    await User.create({
      name: 'User test2',
      email: 'teste2@user.com',
      password,
    });

  }catch(err){
    console.log("err " + err.message);
  }
}

