# Java-Project

Worked on the concept of fault tolerant machines

A set of autonomous machines are moving on the streets of a city. As the machines move, they try to agree on the next action (change of direction) that they should perform. Let’s assume this is a choice between turning left or right(relative to their current direction of motion), at periodic intervals. Machines communicate by sending messages to each other.

Since some of the machines can be faulty, the remaining machines (the correct ones) need to achieve the following objectives:

Objective 1: All the correct machines agree on the next action

Objective 2: If a correct machine proposes an action,then all the correct machines agree on that action
