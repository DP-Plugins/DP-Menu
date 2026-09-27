<center><img src="https://i.postimg.cc/MKPVVR1s/dplogo-512.png" alt="logo"></center>
<center><img src="https://i.postimg.cc/RZ9dqPFx/introduce.png" alt="introduce"></center>

DP-Menu allows server administrators to create and manage **custom GUI menus** in-game.  
Menus can execute various actions when players click items, using the **DPP-Core Action system** to trigger commands, open other menus, give items, run server logic, and more.

---

<center><img src="https://i.postimg.cc/RZ9dqP08/description.png" alt="description"></center>

- Create fully customizable **GUI menus** with multiple rows and pages  
- Edit menu items directly through an **in-game GUI editor**  
- Assign **DPP-Core Actions** to item clicks (command, open menu, give item, etc.)  
- Set **prices** for menu items using economy integration  
- Use **PlaceholderAPI** placeholders in item names and lore  
- Supports command **aliases** to open menus easily  

---

<center><img src="https://i.postimg.cc/rwcjzhpH/depend-plugin.png" alt="depend-plugin"></center>

- All DP-Plugins require the **`DPP-Core`** plugin  
- The plugin will not work if **`DPP-Core`** is not installed  
- You can download **`DPP-Core`** here: <a href="https://github.com/DP-Plugins/DPP-Core/releases" target="_blank">Click me!</a>  
- **PlaceholderAPI** is optional (used for dynamic text)  
- **Essentials / Economy plugin** is optional (used only for price actions)  

---

<center><img src="https://i.postimg.cc/dV01RxJB/installation.png" alt="installation"></center>

1️⃣ Place the **`DPP-Core`** plugin and this plugin file (**`DP-Menu-*.jar`**) into your server’s **`plugins`** folder  

2️⃣ Restart the server, and the plugin will be automatically enabled  

3️⃣ Menu data and language files will be generated automatically  

---

<center><img src="https://i.postimg.cc/jSKcC85K/settings.png" alt="settings"></center>

- **Menu files**  
  - Stored in `plugins/DP-Menu/menus/`  
  - Each menu is saved as an individual `.yml` file  

- **Language files**  
  - `lang/en_US.yml`  
  - `lang/ko_KR.yml`  

- No global `config.yml`; all menu behavior is managed via **commands + DPP-Core actions**  

---

<center><img src="https://i.postimg.cc/SxqdjZKw/command.png" alt="command"></center>

❗ Some commands require admin permission (`dpm.admin`)

**Command List and Examples**

| Command | Permission | Description | Example |
|---|---|---|---|
| `/dpm open <menu>` | dpm.use | Open a menu | `/dpm open MainMenu` |
| `/dpm create <name> <rows>` | dpm.admin | Create a menu | `/dpm create MainMenu 4` |
| `/dpm delete <name>` | dpm.admin | Delete a menu | `/dpm delete MainMenu` |
| `/dpm title <name> <title>` | dpm.admin | Set menu title | `/dpm title MainMenu &6Main Menu` |
| `/dpm row <name> <rows>` | dpm.admin | Change menu rows | `/dpm row MainMenu 5` |
| `/dpm items <name>` | dpm.admin | Edit menu items | `/dpm items MainMenu` |
| `/dpm price <name>` | dpm.admin | Set item prices | `/dpm price MainMenu` |
| `/dpm action <name>` | dpm.admin | Assign DPP-Core actions to items | `/dpm action MainMenu` |
| `/dpm aliases <name> <command>` | dpm.admin | Set command alias | `/dpm aliases MainMenu menu` |
| `/dpm list` | dpm.admin | List all menus | `/dpm list` |
| `/dpm reload` | dpm.admin | Reload menus | `/dpm reload` |

**❗Notes when using commands**

- Menu names must not contain spaces  
- Item edits and action bindings are saved automatically  
- Price actions require an economy plugin  
- Admin commands require **OP** or `dpm.admin` permission  

---

<center><img src="https://i.postimg.cc/Z5ZH0fqL/api-integration.png" alt="api-integration"></center>

**🔧 DPP-Core Action Integration**

DP-Menu is **fully integrated with the DPP-Core Action system**.

Each menu item can execute one or more **Actions** when clicked.  
Actions are managed through `/dpm action <menu>` and stored using DPP-Core’s action framework.

**Examples of supported actions include:**

- Execute console or player commands  
- Open another DP-Menu menu  
- Give items or rewards  
- Check player conditions (permissions, money, etc.)  
- Combine multiple actions in sequence  

This makes DP-Menu a **core hub plugin** that connects other DP plugins and server systems together through a unified action logic.

Additional integrations:
- **PlaceholderAPI** — Used to parse dynamic placeholders in item names and lore  
- **Economy (via DPP-Core MoneyAPI)** — Used for price-based click actions  

---

<center><a href="https://discord.gg/JnMCqkn2FX"><img src="https://i.postimg.cc/4xZPn8dC/discord.png" alt="discord"></a></center>

- https://discord.gg/JnMCqkn2FX  
- Join our Discord for support, bug reports, or feature requests  
- Feedback and improvement ideas are always welcome!

---
