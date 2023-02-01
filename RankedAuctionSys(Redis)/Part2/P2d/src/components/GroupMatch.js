class GroupMatch{

    // Privates should start with #. They are only accessible from inside the class.
    #_groupOfMatch = [];
    #_totalScore = 0;
    #_best_match = [];
 
    // Initialize the constructor, include setter:
    constructor(score_table, group_num)
    {   
        // Define public variables that could be accessed by each member function: 
        this.table = score_table;
        this.group_number = group_num;        
    }

    remove(element, array)
    {
        // Remove specific element from an array:
        for(let i = 0; i < array.length; i++){ 
            if (array[i] === element) { 
                array.splice(i, 1); 
            }
        }
        return array;
    }

    get total_score()
    {
        return this.#_totalScore;
    }


    get best_match()
    {
        // User has not been assigned for a specific group.
        // Initialized as all members are not matched.
        var unmatched = [...Array(this.table.length).keys()];
        do
        {
            unmatched = this.make_group(this.table, unmatched);
        }
        while(unmatched.length > this.group_number);

        // Else: 
        // the numbers of the residual member in the unmatched, 
        // which is lesser than the group_size, We force make them a team.
     
        this.#_best_match.push(unmatched);
    
        return this.#_best_match;
    }

    /**
     * Using Greedy Algorithm to choose the best group of under the current combination.
     * The function will store the temporary result into the record of global combination,
     * and @returns: the residual members which has not matched.
     * @param current_table: 
     * @param unmatched_member: 
     */
    make_group(current_table, unmatched_member)
    {
      
        var all_group = chooseMember(unmatched_member, this.group_number);

        // Intitailize, Go through and Select the group which has the maximum SUM (Best score)
        var group_score = -1;

        var best_group = [];
        for (var i=0; i < all_group.length; i++)
        {   
            var current_score = this.get_score(all_group[i], current_table)
            if( group_score < current_score)
            {
                group_score = current_score;
                // Update the current score
                best_group = all_group[i];
            }
        }

        
        // Push the current best matched group into the global combination for showing later.
        this.#_best_match.push(best_group);
        
        this.#_totalScore = group_score + this.#_totalScore;

        for (var j=0; j < best_group.length; j++)
        {
            unmatched_member = this.remove(best_group[j], unmatched_member);
        }

        return unmatched_member;
    }
    
    // Given a group includes some members, find the total_rank(score) of this group
    get_score(group, datasheet)
    {   
        var _total_score = 0;
        for (let i=0; i < group.length; i++)
        {   
            var member_score = 0;
            var this_member = group[i];
            for (let j=0; j < group.length; j++)
            {   
                if (group[j] !== this_member)
                {   
                    var score = datasheet[group[i]]['git' + group[j]];
                    member_score = score + member_score;
                }
            }
            _total_score = member_score + _total_score;
            
        }
        return _total_score;
    }
}

/**
 * Find All possible combination based on specific group_size by using Recursion.
 * and @returns: the all the combination based on the given Array() of Unmatched members.
 * @param arr_unmatched: The array of the residual unmatched team member.
 * @param all_combination: The maximum group size of each group
 */
// Find All basic combination based on specific group_size
function chooseMember(arr_unmatched, member_num) {
    var all_combination = [];

    (function recursive(arr_unmatched, member_num, result) 
    {
        if (member_num > arr_unmatched.length) 
        {
            return;
        }
        if (member_num === arr_unmatched.length) 
        {
            all_combination.push([].concat(result, arr_unmatched))
        } 
        else 
        {
            for (var i = 0; i < arr_unmatched.length; i++) 
            {
                var newResult = [].concat(result);
                newResult.push(arr_unmatched[i]);

                if (member_num === 1) 
                {
                    all_combination.push(newResult);
                } 
                else 
                {
                    var newArr = [].concat(arr_unmatched);
                    newArr.splice(0, i + 1);
                    recursive(newArr, member_num - 1, newResult);
                }
            }
        }
    })(arr_unmatched, member_num, []);

    return all_combination;
}

export default GroupMatch;