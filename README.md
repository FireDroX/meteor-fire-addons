<div align="center">
  <!-- Logo and Title -->
  <img src="/src/main/resources/assets/meteorfireaddon/icon.png" alt="logo" width="20%"/>
  <h1>Meteor Fire's Addons</h1>
  <p>Some random ideas.</p>
</div>

<hr />

# How to use
- Download the version of the mod you want.
- Put it in your `.minecraft/mods` folder where you have installed Meteor.

*Note: It is recommended to use the [latest dev build](https://meteorclient.com/download?devBuild=latest) of meteor while using rejects*

# Features
## Modules
- AutoReply *(only fetch tellraw)*

# Examples
### AutoReply

- Chat Input        : `AntiScript >> type 8dJe2d9 in chat !`
- Fetch Message     : `AntiScript >> type`
- Reply Message     : `%ARGS_3%`
- Remove Quotations : `false`
- Returned Message  : `8dJe2d9`

<hr />

- Chat Input        : `You have 30 seconds to send the word "Module"`
- Fetch Message     : `You have 30 seconds to send the word`
- Reply Message     : `%ARGS_8%`
- Remove Quotations : `true`
- Returned Message  : `Module`

<hr />

- Chat Input        : `You have 30 seconds to reverse the word "snoddA"`
- Fetch Message     : `You have 30 seconds to reverse the word`
- Reply Message     : `%ARGS-INVERSED_8%`
- Remove Quotations : `true`
- Returned Message  : `Addons`

<hr />