# Cars
*Stop walking, start driving*

Download the latest release [here](https://github.com/celerry/Cars/releases/latest)
---

## Dependencies
* Vault
* An Economy plugin to hook into Vault
* ProtocolLib
* A resource pack with 3D models for the cars!

## Installation
* Download Vault, ProtocolLib, an Economy plugin, and Cars
* Drag and Drop Cars, Vault, ProtocolLib, and an Economy plugin into your server's plugins folder
* Restart your server

## Usage
- Cars can be configured in cars.yml - please pay close attention to the comments at the top or else the plugin may break!
- To spawn a car, create a sign following the sign parameters in config.yml

## Stuff to know
- The "requiredpermission" option in cars.yml is OPTIONAL
- To create a car spawning sign, you require the permission "cars.createsign"

### Commands
- **/cars** - view cars commands
- **/cars lock** - lock the car (only added friends can get in the car)
- **/cars unlock** - unlock the car (everyone can get in the car)
- **/cars add <player>** - add a friend to the car (allows them to drive)
- **/cars remove <player>** - remove a friend from the car
- **/cars despawn** - despawns current car (must be in drivers seat)
- **/cars reset** - removes your car, regardless of whether you're in it or not
- **/cars list** - list all cars
- **/cars buy <id>** - buy a car
- **/cars sell <id>** - sell a car for 75% of its buy price

## Future Plans

In the future I plan on:

- Tidying up the code (this was made in about 4 days so some code is not as optimized as other parts)
- Add fuel
- Removing the 53 car limit (allow unlimited cars)

## License and Libraries used

This plugin uses [FastInv](https://github.com/MrMicky-FR/FastInv) by [MrMicky](https://github.com/MrMicky-FR)\
This project is licensed under [GNU General Public License v3.0](LICENSE)
