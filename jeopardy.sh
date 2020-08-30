#!/bin/bash

#Initialise variables.
declare -a categories
declare -A points
declare -A answers
declare -A questions
declare -a isCategoryCleared

init(){
	score=0
	i=0

	j=0
	k=0
	lenTwo=0
	for filename in categories/*; do
		input="$filename"
		categories[$j]="${filename##*/}"
		while IFS=, read -ra arr; do
			points[$j,$k]="${arr[0]}"
			questions[$j,$k]="${arr[1]}"
			tmp="${arr[2],,}"
			tmp="$(echo -e "${tmp}" | sed -e 's/^[[:space:]]*//' -e 's/[[:space:]]*$//')"
			answers[$j,$k]="$tmp"
			k=$((k+1))
		done < $input

		j=$((j+1))
		lenTwo=$k
		k=0
	done
	lenOne=${#categories[@]}

	for (( z=0; z<lenOne; z++)) ; do
		isCategoryCleared[$z]=false
	done

	return
}
#A function to print the titile menu
print_title_menu() {
	echo "Please select from one of the following options: "
	echo ""
	echo "(p)rint question board"
	echo "(a)sk a question"
	echo "(v)iew current winnings"
	echo "(r)eset game"
	echo "e(x)it"
	echo ""

	return
}

#Function to print question board.
print_question_board() {
	for (( i=0; i<$lenOne; i++ )) ; do
		echo "[$i] ${categories[$i]} : "
		if [ "${isCategoryCleared[$i]}" = true ] ; then
			echo -e "$(tput setaf 3)Completed!$(tput setaf 7)"
		else
			for ((  j=0; j<$lenTwo; j++ )) ; do
				echo "  ${points[$i,$j]}"
			done
			echo ""
		fi
	done

	return
}

ask_a_question() {
	for (( i=0; i< $lenOne; i++)) ; do
		if [ "${isCategoryCleared[$i]}" = false ]; then
			break
		fi

		if [[ $i -eq $((lenOne-1)) ]]; then
			echo "Game completed!"
			reset_game
			return
		fi
	done

	print_question_board
	while true ; do
		read -p "Enter the corresponding number for your chosen category or r to return: " category
		if [ "$category" = "r" ] || [ "$category" = "R" ] ; then
			return
		elif ! [[ "$category" =~ ^[0-9]+$ ]] ; then
			echo "category should be a number"
		elif [ $category -gt $lenOne ] ; then
			echo "Invalid input"
		elif [ "${isCategoryCleared[$category]}" = true ] ; then
			echo "Category cleared"
		else
			break
		fi
	done

	chosenQuestion=-1
	while true ; do
		read -p "Enter the value of your chosen question!: " question
		question=${question/$/}
		for (( point=0; point<$lenTwo; point++ )) ; do
			if [ "$question" == "${points[$category,$point]}" ] ; then
				chosenQuestion=$point
				break
			fi

		done

		if [ $chosenQuestion -eq -1 ] ; then
			echo "Invalid value or the question has been answered"
		else
			break
		fi
	done

	#Ask the chosen question
	echo "${questions[$category,$point]}" | festival --tts
	echo "${questions[$category,$point]}"

	read -p "Enter the answer: " answer
	answer=${answer,,}
	if [[ "$answer" = "${answers[$category,$point]}" ]] ; then
		echo -e "$(tput setaf 2)Correct!!$(tput setaf 7)"
		score=$((score+$question))
	else
		echo -e "$(tput setaf 1)Incorrect!$(tput setaf 7)"
		echo "actual answer is ..."
		echo "${answers[$category,$point]}" | festival --tts
		echo "${answers[$category,$point]}"

		score=$((score-$question))
	fi
	points[$category,$point]="Answered"

	for (( num=0; num<$lenTwo; num++)) ; do
		if [ "${points[$category,$num]}" != "Answered" ] ; then
			break
		fi

		if [ $num -eq $((lenTwo-1)) ] ; then
			isCategoryCleared[$category]=true
		fi
	done

	return
}

view_current_winnings() {
	echo "Your current winning is $ $score"
	return
}

reset_game() {
	while true ; do
		echo "Do you wish to reset the game?"
                read -p "[y/n]" ans

                case $ans in
			[Yy])
				score=0
				init
				print_title_menu
                                break
                                ;;
                        [Nn])
                               	break
                                ;;
                        *)
                               	echo "Please enter valid value"
                                ;;
		esac
	done


	return
}

save_game() {
	while true ; do
		echo "Do you wish to save the game?"
		read -p "[y/n]" ans
		case $ans in
			[Yy])
				echo "game saved!"
				for (( i=0; i<$lenOne; i++)) ; do
					for (( j=0; j<$lenTwo; j++)) ; do
						if [ "${points[$i,$j]}" = "Answered" ] ; then
							echo "$i,$j">>save-data
						fi
					done
				done
				echo "score,$score">>save-data

				break
				;;
			[Nn])
				break
				;;
			*)
				echo "Invalid input!"
				;;
		esac
	done

	return
}

import_game() {
	if ! [[ -f "save-data" ]] ; then
		return
	fi

	while true ; do
		echo "Do you wish to continue game from previous game?"
		read -p "[y/n]" ans
		case $ans in
			[Yy])
				echo "Game imported"
				while IFS=, read -ra arr ; do
					if [ "${arr[0]}" = "score" ] ; then
						score=${arr[1]}
						continue
					fi
					points[${arr[0]},${arr[1]}]="Answered"
				done < save-data
				for (( category=0; category<$lenOne; category++)) ; do
					for (( num=0; num<$lenTwo; num++)) ; do
               					if [ "${points[$category,$num]}" != "Answered" ] ; then
                      					break
               					fi

               					if [ $num -eq $((lenTwo-1)) ] ; then
                       					isCategoryCleared[$category]=true
               					fi
					done
				done

				break
				;;
			[Nn])
				echo "Do you wish to restart a new game?"
				echo "(This will remove the save data)"
				while true ; do
					read -p "[y/n]" answer
					case $answer in
						[Yy])
							echo "Starting a new game..."
							rm save_data
							break
							;;
						[Nn])
							break
							;;
						*)
							echo "Please input valid value"
							;;
					esac
				done
				break
				;;
			*)
				echo "Invalid input!"
				;;
		esac
	done

	return
}

game_over() {
	echo "===================="
	echo "Game Over!!!"
        echo "===================="

	for (( i=0; i<$lenOne; i++ )) ; do
		if [ "${isCategoryCleared[$i]}" = false ] ; then
			save_game
			break

		fi
	done

	return
}

press_anykey_to_continue() {
	read -n 1 -s -r -p "Press any key to continue..."
	echo ""
	return
}
#Main function start here
init
import_game
echo "welcome to jeopardy!" | festival --tts
echo "====================================="
echo "Welcome to Jeopardy!"
echo "====================================="

while true ;
do
	print_title_menu
	read -p "Enter a selection [p/a/v/r/x]: " selection
	case $selection in
		p)
			print_question_board
			press_anykey_to_continue
			;;
		a)
			ask_a_question
			press_anykey_to_continue
			;;
		v)
			view_current_winnings
			press_anykey_to_continue
			;;
		r)
			reset_game
			press_anykey_to_continue
			;;
		x)
			game_over
			break
			;;
		*)
			echo "Please enter either [p/a/v/r/x]"
			press_anykey_to_continue
			;;
	esac
done
exit 0

